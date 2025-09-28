async function addToCart(productId, listingId) {
    let userId = document.cookie
        .split("; ")
        .find(row => row.startsWith("userId"))
        ?.split("=")[1];

    if (!userId) {
        console.log("User id is empty");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/InkSpire/AddToCart", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ productId, listingId, userId })
        });

        const data = await response.json();

        if (data.status) {
            console.log("✅ Added to cart:", data);
            renderCart(data.cart);
            alert(`✅ "${data.cart[data.cart.length - 1].title}" added to cart!`);
        } else {
            alert(data.message);
        }
    } catch (error) {
        console.error("❌ Error adding to cart:", error);
    }
}


let productArray = [];
let subTotal = 0;
let shipping = 300;

async function renderCart(cartItemsFromServer = null) {
    const cartItemsContainer = document.getElementById("cart-items-container");
    const emptyCartContainer = document.getElementById("empty-cart-container");
    const cartSummary = document.getElementById("cart-summery");

    if (!cartItemsContainer || !emptyCartContainer || !cartSummary) return;

    try {
        let cartItems = cartItemsFromServer;

        // If no cart provided, fetch from server
        if (!cartItems) {
            const response = await fetch("http://localhost:8080/InkSpire/ViewCartServlet");
            cartItems = await response.json();
        }


        cartItemsContainer.innerHTML = "";
        productArray = [];
        subTotal = 0;

        if (!cartItems || cartItems.length === 0) {
            emptyCartContainer.style.display = "block";
            cartItemsContainer.style.display = "none";
            cartSummary.style.display = "none";
            return;
        }

        emptyCartContainer.style.display = "none";
        cartItemsContainer.style.display = "block";
        cartSummary.style.display = "block";

        cartItems.forEach(item => {
            subTotal += item.price;

            productArray.push({
                productId: item.productId,
                listingId: item.listingId,
                title: item.title,
                price: item.price,
                image: item.image
            });

            const itemDiv = document.createElement("div");
            itemDiv.classList.add("cart-item");
            itemDiv.innerHTML = `
                <img src="${item.image}" alt="${item.title}" class="cart-item-img">
                <div class="cart-item-details">
                    <h4>${item.title}</h4>
                    <p>Rs ${item.price.toFixed(2)} /=</p>
                    <button class="remove-btn" onclick="removeFromCart(${item.productId})">Remove</button>
                </div>
            `;
            cartItemsContainer.appendChild(itemDiv);
        });

        document.getElementById("subtotal").textContent = `Rs ${subTotal.toFixed(2)} /=`;
        document.getElementById("total").textContent = `Rs ${(subTotal + shipping).toFixed(2)} /=`;
    } catch (err) {
        console.error("❌ Error loading cart:", err);
    }
}

async function removeFromCart(productId) {
    try {
        const response = await fetch("http://localhost:8080/InkSpire/RemoveFromCart", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId })
        });

        const data = await response.json();

        if (data.status) {
            console.log('Product removed:', data.message);
            renderCart();
        } else {
            console.error('Error:', data.message);
        }
    } catch (error) {
        console.error('Request failed:', error);
    }
}



async function startCheckOut(){
        
    const data = JSON.stringify({
        subTotal : subTotal,
        shipping : shipping,
        products : productArray,
        user :  document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1] || null,
        total:subTotal
    });
    
    console.log(data);
    
        const response = await fetch("http://localhost:8080/InkSpire/CheckOut", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: data
        });
        
       if(response.ok){
           
           const json = await response.json();
           
           if(json.status){
               window.location.href = "http://localhost:8080/InkSpire/home/check-out.html";
      
        }else{
               console.log("failed");
           }
           
           
       }else{
           console.log("ERRROR");
       }
    
    
}
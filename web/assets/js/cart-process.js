async function addToCart(productId, listingId) {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {
        try {
            const response = await fetch("http://localhost:8080/InkSpire/AddToCart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({productId, listingId, userId})
            });

            const data = await response.json();

            if (data.status) {
                console.log("✅ Added to cart:", data);
                // Update the cart UI
                renderCart(data.cart);
                console.log(data.cart);
                alert(`✅ "${data.cart[data.cart.length - 1].title}" added to cart!`);
            } else {
                alert(data.message);
            }
        } catch (error) {
            console.error("❌ Error adding to cart:", error);
        }

    } else {
        console.log("User id is Empty");
    }

}



async function renderCart() {
    const cartItemsContainer = document.getElementById("cart-items-container");
    const emptyCartContainer = document.getElementById("empty-cart-container");

    // Stop if we are not on the cart page
    if (!cartItemsContainer || !emptyCartContainer)
        return;

    try {
        const response = await fetch("/InkSpire/ViewCartServlet");
        const cartItems = await response.json();

        cartItemsContainer.innerHTML = "";

        if (!cartItems || cartItems.length === 0) {
            emptyCartContainer.style.display = "block";
            cartItemsContainer.style.display = "none";
            return;
        }

        emptyCartContainer.style.display = "none";
        cartItemsContainer.style.display = "block";

        let subtotal = 0;

        cartItems.forEach(item => {
            subtotal += item.price;

            const itemDiv = document.createElement("div");
            itemDiv.classList.add("cart-item");
            itemDiv.innerHTML = `
                <img src="${item.image}" alt="${item.title}" class="cart-item-img">
                <div class="cart-item-details">
                    <h4>${item.title}</h4>
                    <p>$${item.price.toFixed(2)}</p>
                </div>
            `;
            cartItemsContainer.appendChild(itemDiv);
        });

        document.getElementById("subtotal").textContent = `$${subtotal.toFixed(2)}`;
        document.getElementById("total").textContent = `$${(subtotal + 10).toFixed(2)}`;
    } catch (err) {
        console.error("❌ Error loading cart:", err);
    }
}


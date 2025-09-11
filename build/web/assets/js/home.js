
async function loadSection(file, targetId) {
    try {
        const response = await fetch(file);
        if (!response.ok) {
            throw new Error(`Failed to load ${file}`);
        }

        const htmlContent = await response.text();
        document.getElementById(targetId).innerHTML = htmlContent;
    } catch (error) {
        console.error(`Error loading ${file}:`, error);
    }
}


 function loadHtml() {
   loadSection("Header.html", "header-div");
    loadSection("footer.html", "footer-content");
    loadCategories();

}



function sideNav() {
    const navToggle = document.getElementById("navToggle");
    const sideMenu = document.getElementById("sideMenu");
    const overlay = document.getElementById("overlay");
    const closeBtn = document.getElementById("closeMenu");

    if (!navToggle || !sideMenu || !overlay || !closeBtn) {
        console.error("One or more elements not found:", {
            navToggle,
            sideMenu,
            overlay,
            closeBtn
        });
    }

    function openMenu() {
        sideMenu.classList.add("open");
        overlay.classList.add("show");
        sideMenu.setAttribute("aria-hidden", "false");
        overlay.setAttribute("aria-hidden", "false");
        navToggle.setAttribute("aria-expanded", "true");
        document.body.style.overflow = "hidden";
    }

    function closeMenu() {
        sideMenu.classList.remove("open");
        overlay.classList.remove("show");
        sideMenu.setAttribute("aria-hidden", "true");
        overlay.setAttribute("aria-hidden", "true");
        navToggle.setAttribute("aria-expanded", "false");
        document.body.style.overflow = "";
    }

    navToggle.addEventListener("click", openMenu);
    closeBtn.addEventListener("click", closeMenu);
    overlay.addEventListener("click", closeMenu);

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape")
            closeMenu();
    });
}  


async function loadCategories(){
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadCategory");
    console.log("hello");
    
    if(response.ok){
        
        const json = await response.json();
        
        if(json.status){
            
          const ul =  document.getElementById("category-ul");
            
            json.categoryList.forEach(item => {
               const li = document.createElement("li");
               li.value = item.id;
               li.innerHTML = `<i class="bi bi-book"></i> ${item.value}`;
               ul.appendChild(li);
                
               console.log(json.categoryList);
                
            });
            
        }else{
            console.log("Error ss");
            
        }

        
    }else{
          console.log("Error sfdfds");
        
    }
    
}


async function loadDailyDeals(){
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadDailyDeals");
    
    if(response.ok){
        
        const json = await response.json();
        
        
        if(json.dailyDeals && json.dailyDeals.length > 0){
              renderProducts(json.dailyDeals);
        }else{
              $.notify("Daily Deals not Found !", "error");
        }
        
        
    }else{
          $.notify("Network Error Please Try again later!", "error");
    }
    
    
}

function renderProducts(products) {
    const container = document.getElementById("product-container");
    container.innerHTML = ""; // Clear previous content

    products.forEach(product => {
        const card = document.createElement("div");
        card.classList.add("product-card");

        card.innerHTML = `
            <img src="${product.imageUrl}" alt="${product.productName}" class="product-image"/>
            <div class="product-content">
                <h2 class="product-title">${product.productName}</h2>
                <p class="product-desc">${product.productCategory}</p>
                <span class="stock-status">In Stock</span>
                <span class="price">Rs. ${product.productPrice}</span>
                <div class="product-actions">
                    <button class="add-to-cart">
                        <i class="bi bi-heart-fill"></i> Add to Cart
                    </button>
                    <button class="buy-btn">Buy Now</button>
                </div>
            </div>
        `;

        container.appendChild(card);
    });
}


async function loadLatestArrivals(){
    
     const response = await fetch("http://localhost:8080/InkSpire/LoadLatestProducts");
     
        if(response.ok){
        
        const json = await response.json();
        
        
        if(json.latestListings && json.latestListings.length > 0){
              renderLatestProducts(json.latestListings);
        }else{
              $.notify("Latest Arrivals not Found !", "error");
        }
        
        
    }else{
          $.notify("Network Error Please Try again later!", "error");
    }
     
     
    
}

function renderLatestProducts(products) {
    const container = document.getElementById("latest-arrivals-grid");
    container.innerHTML = ""; // Clear previous content

    products.forEach(product => {
        const card = document.createElement("div");
        card.classList.add("latest-card");

        card.innerHTML = `
            <img src="${product.imageUrl}" alt="${product.productName}" class="latest-image"/>
            <div class="latest-content">
                <h2 class="latest-title">${product.productName}</h2>
                <p class="latest-desc">${product.productCategory}</p>
                <span class="stock-status">In Stock</span>
                <span class="latest-price">Rs. ${product.productPrice}</span>
                <div class="latest-actions">
                    <button class="add-to-cart">
                        <i class="bi bi-heart-fill"></i> Add to Cart
                    </button>
                    <button class="buy-btn">Buy Now</button>
                </div>
            </div>
        `;

        container.appendChild(card);
    });
}
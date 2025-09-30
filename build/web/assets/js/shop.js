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


async function loadHtml() {
   await loadSection("Header.html", "header-div");
   await  loadSection("footer.html", "footer-content");

}

document.addEventListener("DOMContentLoaded", async function(){
   await loadHtml();
    loadLatestShopArrivals();
    loadShopDailyDeals();
    productFilterBy(1);
});



async function loadLatestShopArrivals(){
     
       const limit = 12;
       const response = await fetch(`http://localhost:8080/InkSpire/LoadLatestProducts?limit=${limit}`);
     
        if(response.ok){
        
        const json = await response.json();
        const grid = "shop-product-grid";
        
        if(json.latestListings && json.latestListings.length > 0){
              renderLatestShopProducts(json.latestListings ,grid );
        }else{
              $.notify("Latest Arrivals not Found !", "error");
        }
        
        
    }else{
          $.notify("Network Error Please Try again later!", "error");
    }
     
     
    
}

function renderLatestShopProducts(products , grid) {
  const container = document.getElementById(grid);
  if (!container) {
    console.error("❌ #shop-product-grid not found in DOM");
    return;
  }

  container.innerHTML = ""; // clear old cards if re-render

  products.forEach(product => {
    const card = document.createElement("div");
    card.classList.add("shop-product-card");

    card.innerHTML = `
      <img src="${product.imageUrl}" alt="${product.productName}" class="shop-product-image"/>
      <div class="shop-product-content">
          <h2 class="shop-product-title">${product.productName}</h2>
          <p class="shop-product-desc">${product.productCategory}</p>
          <span class="shop-stock-status">In Stock</span>
          <span class="shop-price">Rs. ${product.productPrice}</span>
          <div class="shop-product-actions">
              <button class="shop-add-to-cart" onclick="addToCart('${product.proId}' , '${product.id}');"> <i class="bi bi-heart-fill"></i> Add</button>
              <button class="shop-buy-now" onclick="loadSingleView('${product.id}');" >Buy</button>
          </div>
      </div>
    `;

    container.appendChild(card);
  });
}


async function loadShopDailyDeals(){
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadDailyDeals");
    
    if(response.ok){
        
        const json = await response.json();
         const grid = "shop-daily-grid";
        
        if(json.dailyDeals && json.dailyDeals.length > 0){
                   
              renderLatestShopProducts(json.dailyDeals , grid);
        }else{
              $.notify("Daily Deals not Found !", "error");
        }
        
        
    }else{
          $.notify("Network Error Please Try again later!", "error");
    }
    
    
}


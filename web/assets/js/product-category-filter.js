async function productFilterBy(categoryId){

     
       const response = await fetch(`http://localhost:8080/InkSpire/LoadCategoryBooks?categoryId=${categoryId}`);
     
        if(response.ok){
     
        const json = await response.json();
        const grid = "shop-fiction-grid";
                console.log(json.productList);
                
        if(json.status && json.productList){
              renderLatestFictionProducts(json.productList ,grid );
        }else{
              
        }
        
        
    }else{
         
    }

}

function renderLatestFictionProducts(products, grid) {
  const container = document.getElementById(grid);
  if (!container) return;

  container.innerHTML = "";

  products.forEach(product => {
    // support both backend JSON formats
    const name = product.productName || product.title;
    const category = product.productCategory || product.category;
    const price = product.productPrice || product.price;

    const card = document.createElement("div");
    card.classList.add("shop-product-card");

    card.innerHTML = `
      <img src="${product.imageUrl}" alt="${name}" class="shop-product-image"/>
      <div class="shop-product-content">
          <h2 class="shop-product-title">${name}</h2>
          <p class="shop-product-desc">${category}</p>
          <span class="shop-stock-status">In Stock</span>
          <span class="shop-price">Rs. ${price}</span>
          <div class="shop-product-actions">
              <button class="shop-add-to-cart"><i class="bi bi-heart-fill"></i> Add</button>
              <button class="shop-buy-now">Buy</button>
          </div>
      </div>
    `;

    container.appendChild(card);
  });
}




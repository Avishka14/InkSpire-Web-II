document.addEventListener("DOMContentLoaded", () => {
  const tabs = document.querySelectorAll(".sidebar li");
  const panels = document.querySelectorAll(".tab-panel");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      tab.classList.add("active");

      panels.forEach(p => p.classList.remove("active"));
      document.getElementById(tab.dataset.tab).classList.add("active");
    });
  });
});


// Toggle offer listings
$(document).on("click", ".toggle-listings", function() {
  let listings = $(this).closest(".offer-card").find(".offer-listings");
  listings.toggleClass("hidden");

  $(this).text(listings.hasClass("hidden") ? "View Listings" : "Hide Listings");
});

// Delete offer
$(document).on("click", ".delete-offer-btn", function() {
  let offerId = $(this).closest(".offer-card").data("offer-id");
  $.notify("Offer " + offerId + " deleted", "success");
  $(this).closest(".offer-card").remove();
});

// Assign product to offer
$("#assignProductBtn").on("click", function() {
  let productId = $("#productIdInput").val();
  let offerId = $("#offerSelect").val();

  if (productId && offerId) {
    $.notify("Product " + productId + " assigned to offer " + offerId, "success");
    $("#productIdInput").val("");
  } else {
    $.notify("Please enter product ID and select an offer", "error");
  }
});


async function loadListingData(){
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadListingData");
    
    if(response.ok){
        const json = await response.json();
        
        loadSelect("category" , json.categoryList , "value");
        loadSelect("condition" , json.conditionList , "value");
        
    }else{
        console.log("error");
    }
    

}

function loadSelect(selectId , list , property){
    
    const select = document.getElementById(selectId);
    
    list.forEach(item => {      
       const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);
        
    });
    
}

async function saveProductAdmin() {
    const productId = Math.floor(100000 + Math.random() * 900000);
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();
    const categoryId = document.getElementById("category").value;
    const conditionId = document.getElementById("condition").value;
    const itemAvailabilityId = "1";
    const price = document.getElementById("price").value.trim();
    const sellerId = "1";
    const approvalId = "1";
    const image1 = document.getElementById("img1").files[0];

    // 🚨 Frontend validation
    if (!title || !description || !categoryId || !conditionId || !price || !image1) {
        $.notify("All fields including product image are required.", "error");
        return;
    }

    const form = new FormData();
    form.append("productId", productId);
    form.append("title", title);
    form.append("description", description);
    form.append("categoryId", categoryId);
    form.append("conditionId", conditionId);
    form.append("itemAvailabilityId", itemAvailabilityId);
    form.append("sellerId", sellerId);
    form.append("price", price);
    form.append("approvalId", approvalId);
    form.append("image1", image1);

    try {
        const response = await fetch("http://localhost:8080/InkSpire/AddProduct", {
            method: "POST",
            body: form
        });

        if (!response.ok) {
            $.notify("Server error. Please try again later.", "error");
            return;
        }

        const json = await response.json();

        if (json.status) {
            $.notify(json.message || "Listing added successfully!", "success");
            clearProductForm();
        } else if (json.message === "4004") {
            $.notify("All fields are required to add a listing", "error");
        } else {
            $.notify(json.message || "Listing addition failed!", "error");
        }
    } catch (error) {
        console.error("Request failed:", error);
        $.notify("Network error. Please check your connection.", "error");
    }
}




function clearProductForm() {
    document.getElementById("title").value = "";
    document.getElementById("description").value = "";
    document.getElementById("category").value = "";
    document.getElementById("condition").value = "";
    document.getElementById("price").value = "";
    document.getElementById("img1").value = "";
}


async function addNewCategory(){
    
    
    const newCateJson = JSON.stringify({
        newCategory:document.getElementById("new-category-input").value  
    });
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadCategory" , {
        method:"POST",
        header:{
           "Content-Type":"application/json"
        },
        body:newCateJson
    });
    
    if(response.ok){
         const json = await response.json();
         
         if(json.status){
              $.notify("Successfully Added Category.", "success");
        
        }else{
              $.notify( json.message|| "Network error. Please check your connection.", "error");
         }
        
        
    }else{
          $.notify("Network error. Please check your connection.", "error");
    }
    
    
}
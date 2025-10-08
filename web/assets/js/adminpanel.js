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




async function loadListingData() {

    const response = await fetch("http://localhost:8080/InkSpire/LoadListingData");

    if (response.ok) {
        const json = await response.json();

        loadSelect("category", json.categoryList, "value");
        loadSelect("condition", json.conditionList, "value");

    } else {
        console.log("error");
    }


}

function loadSelect(selectId, list, property) {

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
    document.getElementById("price").value = "";
    document.getElementById("img1").value = "";
}


async function addNewCategory() {

    const newCateJson = JSON.stringify({
        newCategory: document.getElementById("new-category-input").value
    });

    const response = await fetch("http://localhost:8080/InkSpire/LoadCategory", {
        method: "POST",
        header: {
            "Content-Type": "application/json"
        },
        body: newCateJson
    });

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            $.notify("Successfully Added Category.", "success");

        } else {
            $.notify(json.message || "Network error. Please check your connection.", "error");
        }


    } else {
        $.notify("Network error. Please check your connection.", "error");
    }


}


async function addNewOffers() {

    const newOffer = JSON.stringify({
        newOffer: document.getElementById("offerValue").value
    });

    const response = await fetch("http://localhost:8080/InkSpire/AddOffer", {
        method: "POST",
        header: {
            "Content-Type": "application/json"
        },
        body: newOffer

    });

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            $.notify(json.message, "success");
        } else {
            $.notify(json.message, "error");
        }


    } else {
        $.notify("Network error. Please check your connection.", "error");
    }

}

async function loadOffers() {
    try {
        const response = await fetch("http://localhost:8080/InkSpire/AddOffer");

        if (!response.ok) {
            console.error("Network error: ", response.status);
            return;
        }

        const json = await response.json();

        if (!json.status) {
            $.notify("Network error. Please check your connection.", "error");
            return;
        }

        const container = document.getElementById("offersList");
        container.innerHTML = "";

        json.offerList.forEach(offer => {

            const card = document.createElement("div");
            card.className = "offer-card";
            card.dataset.offerId = offer.id;


            card.innerHTML = `
                <div class="offer-header">
                    <h3> ${offer.offerTypeName}</h3>
                </div>
                <div class="offer-actions">
                    <button class="bre-button small toggle-listings">View Listings</button>
                    <button class="danger bre-button small delete-offer-btn">Delete</button>
                </div>
                <div class="offer-listings hidden">
                    <h4>Listings under this offer:</h4>
                    <ul>
                        ${offer.listings.map(listing => `<li>Product ID: ${listing.id} – ${listing.productTitle}</li>`).join('')}
                    </ul>
                </div>
            `;


            container.appendChild(card);


            const toggleBtn = card.querySelector(".toggle-listings");
            const listingsDiv = card.querySelector(".offer-listings");
            toggleBtn.addEventListener("click", () => {
                listingsDiv.classList.toggle("hidden");
            });


            const deleteBtn = card.querySelector(".delete-offer-btn");
            deleteBtn.addEventListener("click", async () => {
                if (confirm("Are you sure you want to delete this offer?")) {

                    const response = await fetch(`http://localhost:8080/InkSpire/AddOffer?id=${offer.id}`, {
                        method: "DELETE"
                    });

                    const json = await response.json();

                    if (json.status) {
                        $.notify("Offer deleted successfully.", "success");
                        loadOffers();

                    } else {
                        $.notify(json.message || "Failed to delete offer.", "error");
                    }

                }
            });
        });

    } catch (error) {
        console.error("Error loading offers: ", error);
        $.notify("An unexpected error occurred.", "error");
    }
}

async function loadSelectorOffers() {

    const response = await fetch("http://localhost:8080/InkSpire/LoadOffers");

    if (response.ok) {

        const json = await response.json();

        const select = document.getElementById("offerSelect");

        json.offerTypeList.forEach(item => {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item.value;
            option.id = item.id;
            select.appendChild(option);

        });

    } else {
        $.notify("An unexpected error occurred.", "error");
    }


}

async function addNewProductOffer() {

    const select = document.getElementById("offerSelect");
    const selectedOption = select.options[select.selectedIndex];

    const newOffer = JSON.stringify({
        offerProduct: document.getElementById("productIdInput").value,
        offerId: selectedOption.id
    });

    console.log(newOffer);


    const response = await fetch("http://localhost:8080/InkSpire/LoadOffers", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: newOffer
    });

    if (response.ok) {
        const json = await response.json();

        if (json.status) {
            $.notify("Successfully Added Offer. Please Refresh!", "success");
            await  loadOffers();

        } else {
            $.notify(json.message || "Network error. Please check your connection.", "error");
        }


    } else {
        $.notify("Network error. Please check your connection.", "error");
    }



}


function openEditPopup() {

    document.getElementById("viewPopUp").classList.add("active");

}

function closeEditPopup() {
    document.getElementById("viewPopUp").classList.remove("active");
}


async function loadListingApprovalAdmin() {

    const response = await fetch("http://localhost:8080/InkSpire/LoadListingApprovalDataAdmin");

    if (response.ok) {
        const json = await response.json();

        if (json.listingArray) {

            const container = document.getElementById("listings-grid");
            container.innerHTML = "";

            json.listingArray.forEach(listing => {
                const card = document.createElement("div");
                card.className = "listing-card";
                card.dataset.date = new Date().toISOString().split('T')[0];
                card.dataset.price = listing.price;

                const listingData = encodeURIComponent(JSON.stringify(listing));

                card.innerHTML = `
                    <img src="${listing.imageUrl}" alt="${listing.title}" style="width:220px; height:150px;">
                    <h3>${listing.title}</h3>
                    <p class="price">LKR ${listing.price}</p>
                    <p>Seller: ${listing.seller}</p>
                    <p>Product ID: ${listing.productId}</p>
                    <div class="button-group">
                        <button class="grey-button" onclick="loadView('${listingData}')">View</button>
                        <button class="bre-button" onclick="quickApprove('${listingData}')">Approve</button>
                        <button class="danger bre-button" onclick="declineListing(${listing.listingId});">Decline</button>
                    </div>
                `;

                container.appendChild(card);
            });

        } else {
            $.notify("Listing Loading Failed!", "error");
        }


    } else {
        $.notify("Network error. Please check your connection.", "error");
    }

}


function loadView(listingStr) {
    const listing = JSON.parse(decodeURIComponent(listingStr));
    console.log("Listing ID:", listing.listingId);
    
    document.getElementById("viewPopUp").classList.add("active");
    
    document.getElementById("seller-viewx-up").value = listing.seller || "Seller Not Found";
    document.getElementById("title-viewx-up").value = listing.title || "Title Not Found";
    document.getElementById("description-viewx-up").value = listing.desc || "Description Not Found";
    document.getElementById("price-viewx-up").value = listing.price || "Price Not Found";
    document.getElementById("img-viewx").src = listing.imageUrl ;
    document.getElementById("category-viewx-up").innerHTML = listing.category ;
    document.getElementById("condition-viewx-up").innerHTML = listing.condition ;

}


async function quickApprove(listingStr){
    
    const listing = JSON.parse(decodeURIComponent(listingStr));
    console.log("Listing ID:", listing.listingId);
    
    
    
    
}
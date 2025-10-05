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


async function loadHeadFoot() {
    loadSection("Header.html", "header-area");
    loadSection("footer.html", "footer-area");

}

// Sidebar tab switcher
document.querySelectorAll(".sidebar li").forEach(tab => {
    tab.addEventListener("click", () => {
        document.querySelectorAll(".sidebar li").forEach(li => li.classList.remove("active"));
        tab.classList.add("active");
        let target = tab.dataset.tab;
        document.querySelectorAll(".tab-panel").forEach(panel => panel.classList.remove("active"));
        document.getElementById(target).classList.add("active");
    });
});

// Messaging functionality
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendMessage");
const messagesArea = document.getElementById("messagesArea");

function sendMessage() {
    const text = messageInput.value.trim();
    if (text === "")
        return;

    // create message element
    const msg = document.createElement("div");
    msg.classList.add("message", "sent");
    msg.textContent = text;
    messagesArea.appendChild(msg);

    // auto-scroll down
    messagesArea.scrollTop = messagesArea.scrollHeight;

    // clear input
    messageInput.value = "";
}

sendBtn.addEventListener("click", sendMessage);
messageInput.addEventListener("keypress", e => {
    if (e.key === "Enter") {
        e.preventDefault();
        sendMessage();
    }
});

async function sendMail() {

    const data = {
        email: document.getElementById("email-seller").value
    };

    const mailAddress = JSON.stringify(data);

    console.log(mailAddress);

    const response = await fetch("http://localhost:8080/InkSpire/MailServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: mailAddress
    });

    if (response.ok) {
        const result = await response.json();

        if (result.status) {

            $(".grey-button").notify("Code Has send to your email!", "success");

        } else {
            $(".grey-button").notify("Invalid E-Mail Address", "error");
        }
    } else {
        $(".grey-button").notify("Something went wrong Please try again Later", "error");
    }

}

async function verifyCode() {


    const userCode = document.getElementById("code").value;

    console.log(userCode);

    const response = await fetch("http://localhost:8080/InkSpire/VerifyCodeServlet", {

        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "userCode=" + encodeURIComponent(userCode),
        credentials: "include"
    });

    const result = await response.text();

    if (result === "Verification successful!") {
        document.getElementById("get-code").style.display = "none";
        document.getElementById("seller-activate-section").style.display = "block";

    } else {
        $(".btncode").notify("Verification Code is Invalid !", "error");
    }


}

async function activateSellerAccount() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const data = JSON.stringify({
            username: document.getElementById("username").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/VerifySellerAccount?id=${encodeURIComponent(userId)}`,
                {
                    method: "POST",
                    body: data,
                    header: {
                        "Content-Type": "application/json"
                    }
                }

        );

        if (response.ok) {

            const json = await response.json();

            if (json.status) {
                $(".btnac").notify("Seller Account Acivated Successfully", "success");

            } else {
                $(".btnac").notify(json.message, "error");
            }


        } else {
            $(".btnac").notify("Network Error Please try again Later", "error");
        }

    } else {
        $(".btnac").notify("Please Log In Again!", "error");
    }


}


async function loadSellerInfo() {


    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const response = await fetch(`http://localhost:8080/InkSpire/LoadSellerBaseInfo?id=${encodeURIComponent(userId)}`);

        if (response.ok) {

            const json = await response.json();

            if (json.status) {

                if (json.seller) {
                    console.log(json.seller);
                    document.getElementById("account-layout").style.display = "flex";
                    document.getElementById("seller-profile-not-layout").style.display = "none";

                } else {
                    document.getElementById("account-layout").style.display = "none";
                    document.getElementById("seller-profile-not-layout").style.display = "block";

                }

            } else {
                $(".breadcrumb").notify(json.message, "error");
            }


        } else {
            $(".breadcrumb").notify("Network Error Please try again Later", "error");
        }


    } else {
        $(".breadcrumb").notify("Network Error Please try again Later", "error");
    }

}


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


async function addNewListing() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("sellerId"))
            ?.split("=")[1];

    if (userId) {

        const productId = Math.floor(100000 + Math.random() * 900000);
        const title = document.getElementById("title").value.trim();
        const description = document.getElementById("description").value.trim();
        const categoryId = document.getElementById("category").value;
        const conditionId = document.getElementById("condition").value;
        const itemAvailabilityId = "1";
        const price = document.getElementById("price").value.trim();
        const sellerId = userId;
        const approvalId = "2";
        const image1 = document.getElementById("img1").files[0];

        if (!title || !description || !categoryId || !conditionId || !price || !image1) {
            $(".adlst").notify("All fields including product image are required.", "error");
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
                $(".adlst").notify("Server error. Please try again later.", "error");
                return;
            }

            const json = await response.json();

            if (json.status) {
                $(".adlst").notify("Listing added successfully!", "success");
                clearProductForm();
            } else if (json.message === "4004") {
                $(".adlst").notify("All fields are required to add a listing", "error");
            } else {
                $(".adlst").notify(json.message || "Listing addition failed!", "error");
            }
        } catch (error) {
            console.error("Request failed:", error);
            $(".adlst").notify("Network error. Please check your connection.", "error");
        }

    } else {
        $(".adlst").notify("Seller Acccount Please try again Later", "error");
    }

}

function clearProductForm() {
    document.getElementById("title").value = "";
    document.getElementById("description").value = "";
    document.getElementById("price").value = "";
    document.getElementById("img1").value = "";
}


async function loadExistingListings() {

    let sellerId = document.cookie
            .split("; ")
            .find(row => row.startsWith("sellerId"))
            ?.split("=")[1];

    if (sellerId) {

        const response = await fetch(`http://localhost:8080/InkSpire/LoadSellerListings?id=${sellerId}`);

        if (response.ok) {

            const json = await response.json();

            if (json.status) {

                console.log(json.listingList);

                const container = document.querySelector(".pend-listings-grid");

                if (!container) {
                    console.error("No container element found for listings!");
                    return;
                }

                container.innerHTML = "";

                if (json.status && Array.isArray(json.listingList)) {
                    json.listingList.forEach(listing => {
                        const card = document.createElement("div");
                        card.className = "listing-card";
                        card.setAttribute("data-name", listing.title);
                        card.setAttribute("data-price", listing.price);
                        card.setAttribute("data-status", listing.status.toLowerCase());

                        card.innerHTML = `
                            <p class="date">Added Date and Time : <span>${new Date().toLocaleString()}</span></p>
                            <img src="${listing.imageUrl}" alt="${listing.title}">
                            <h3>${listing.title}</h3>
                            <p class="price">LKR ${Number(listing.price || 0).toFixed(2)}</p>
                            <p>Status: <span class="status ${listing.status.toLowerCase()}">${listing.status}</span></p>
                            <div class="li-btn-con">
                            <button class="edit-btn bre-button ">Edit</button>
                            <button class="grey-button ">Mark as Unavailable</button>
                            </div>

                        `;

                        container.appendChild(card);
                    });
                } else {
                    $(".ti-ed-li").notify(json.message || "No listings found", "error");
                }


            } else {
                $(".ti-ed-li").notify(json.message, "error");
            }

        } else {
            $(".ti-ed-li").notify("Network Error Please try again Later", "error");
        }


    } else {
        $(".ti-ed-li").notify("Seller Acccount Error Please try again Later", "error");
    }

}
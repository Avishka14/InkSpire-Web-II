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


async function loadMyAccHeader() {
    loadSection("Header.html", "header-section");
    loadSection("footer.html", "footer-section");
    loadUserDataFields();
}



$(document).ready(function () {

    $(".sidebar li").on("click", function () {
        let tabId = $(this).data("tab");

        $(".sidebar li").removeClass("active");
        $(this).addClass("active");
        $(".tab-panel").removeClass("active").hide();
        $("#" + tabId).addClass("active").show();
    });


    $(".tab-panel").hide();
    $(".tab-panel.active").show();
});





async function loadUserDataFields() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const response = await fetch(`http://localhost:8080/InkSpire/ReturnUserData?id=${encodeURIComponent(userId)}`);

        if (response.ok) {

            const json = await response.json();

            if (json.status) {


                if (json.user) {
                    console.log(json.user);

                    document.getElementById("fName").value = json.user?.firstName || "First Name not Found";
                    document.getElementById("Lname").value = json.user?.lastName || "Last Name not Found";

                    let dateOb = new Date(json.user.regDate);
                    let formatted = dateOb.toISOString().split("T")[0];

                    document.getElementById("regDate").value = formatted || "Date not Found";

                    document.getElementById("email").value = json.user?.email || "E-Mail not Found";
                    document.getElementById("mobileNumber").value = json.user?.mobile || "Mobile not Found";


                } else {
                    $("#notify").notify("Please Log In Again!", "error");
                }


            } else {
                $("#notify").notify(json.message, "error");
            }


        } else {
            $("#notify").notify("Network Error Please Try again later!", "error");
        }


    } else {
        $("#notify").notify("Please Log In Again!", "error");
    }

}


async function updateUserBaseInfo() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const data = JSON.stringify({
            firstName: document.getElementById("fName").value,
            laststName: document.getElementById("Lname").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/UpdateUserData?id=${encodeURIComponent(userId)}`,
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
                $(".sv-btn").notify(json.message, "success");
                loadUserDataFields();
            } else {
                $(".sv-btn").notify(json.message, "error");
            }


        } else {
            $(".section").notify("Metwork Error Please try again Later", "error");
        }

    } else {
        $(".section").notify("Please Log In Again!", "error");
    }

}

async function updateUserEmail() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const data = JSON.stringify({
            email: document.getElementById("email").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/UpdateUserEmail?id=${encodeURIComponent(userId)}`,
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
                $(".e-btn").notify(json.message, "success");
                loadUserDataFields();
            } else {
                $(".e-btn").notify(json.message, "error");
            }


        } else {
            $(".section").notify("Network Error Please try again Later", "error");
        }

    } else {
        $(".section").notify("Please Log In Again!", "error");
    }

}

async function updateUserContact() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const data = JSON.stringify({
            contact: document.getElementById("mobileNumber").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/UpdateUserMobile?id=${encodeURIComponent(userId)}`,
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
                $(".c-btn").notify(json.message, "success");
                loadUserDataFields();
            } else {
                $(".c-btn").notify(json.message, "error");
            }


        } else {
            $(".section-b").notify("Network Error Please try again Later", "error");
        }

    } else {
        $(".section-b").notify("Please Log In Again!", "error");
    }

}

async function loadCitySelection() {

    const response = await fetch("http://localhost:8080/InkSpire/LoadCityUser");

    if (response.ok) {

        const json = await response.json();

        const select = document.getElementById("city-select");

        json.categoryList.forEach(item => {
            const option = document.createElement("option");
            option.value = item.id;
            option.innerHTML = item["name"];
            select.appendChild(option);



        });


    } else {
        $(".address").notify("Network Error Please try again Later", "error");
    }


}


async function loadUserAddress() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const response = await fetch(`http://localhost:8080/InkSpire/LoadUserAddress?id=${encodeURIComponent(userId)}`);

        if (response.ok) {

            const json = await response.json();

            if (json.status) {

                if (json.address) {
                    console.log(json.address);

                    document.getElementById("line1").value = json.address?.line1 || "Street Not Found";
                    document.getElementById("line2").value = json.address?.line2 || "Line 2 Not Found";

                    const citySelect = document.getElementById("city-select");
                    const cityFromJson = json.address?.city || "";
                    const cityId = json.address?.cityId;

                    let optionExists = [...citySelect.options].some(opt => opt.value === cityFromJson);

                    if (!optionExists && cityFromJson) {
                        let newOption = document.createElement("option");
                        newOption.value = cityId;
                        newOption.text = cityFromJson;
            
                        citySelect.appendChild(newOption);
                    }

                    citySelect.value = cityId;

                    document.getElementById("postl-code").value = json.address?.postal || "Postal Code Not Found";

                } else {
                    $(".address").notify("Please Log In Again!", "error");
                }


            } else {
                $(".address").notify(json.message, "error");
            }


        } else {
            $(".address").notify("Network Error Please try again Later", "error");
        }


    } else {
        $(".address").notify("Network Error Please try again Later", "error");
    }


}


async function updateUserAddress(){
    
     let shipId = document.cookie
            .split("; ")
            .find(row => row.startsWith("shippingAdId"))
            ?.split("=")[1];

    if (shipId) {

        const data = JSON.stringify({
            line1: document.getElementById("line1").value,
            line2: document.getElementById("line2").value,
            city: document.getElementById("city-select").value,
            postal: document.getElementById("postl-code").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/UpdateUserAddress?id=${encodeURIComponent(shipId)}`,
                {
                    method: "POST",
                    body: data,
                    header: {
                        "Content-Type": "applicatidon/json"
                    }
                }

        );

        if (response.ok) {

            const json = await response.json();

            if (json.status) {
                $(".a-btn").notify(json.message, "success");
                loadUserDataFields();
            } else {
                $(".a-btn").notify(json.message, "error");
            }


        } else {
            $(".address").notify("Network Error Please try again Later", "error");
        }

    } else {
        $(".address").notify("Please Log In Again!", "error");
    }
    
    
    
}
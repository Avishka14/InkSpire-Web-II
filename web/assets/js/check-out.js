
let currentStep = 1;
const totalSteps = 3;
const stepForms = [
    document.getElementById('check-out-step-1'),
    document.getElementById('check-out-step-2'),
    document.getElementById('check-out-step-3')
];


function updateStepIndicator() {
    for (let i = 1; i <= totalSteps; i++) {
        const stepStatus = document.getElementById(`step-${i}-status`);

        if (!stepStatus)
            continue;

        stepStatus.classList.remove('active', 'completed');

        if (i === currentStep) {
            stepStatus.classList.add('active');
        } else if (i < currentStep) {
            stepStatus.classList.add('completed');
        }
    }
}


function nextStep(step) {
    const currentForm = stepForms[currentStep - 1];

    if (currentForm && !currentForm.checkValidity()) {
        currentForm.reportValidity();
        return;
    }

    showStep(step);
}

function prevStep(step) {
    showStep(step);
}

function showStep(step) {

    if (step < 1 || step > totalSteps)
        return;


    stepForms.forEach(form => {
        if (form)
            form.classList.add('hidden');
    });

    const formToShow = stepForms[step - 1];
    if (formToShow) {
        formToShow.classList.remove('hidden');
        currentStep = step;
        updateStepIndicator();
        window.scrollTo({top: 0, behavior: 'smooth'});
    }
}

function removeFromCart(productId) {
    console.log(`Removed item with ID: ${productId}`);
    alertMessage(`Item ${productId} removed from cart! (Mock action)`, 'red');
}

function alertMessage(message, type = 'blue') {
    const messageBoxContainer = document.getElementById('custom-message-box');
    if (!messageBoxContainer)
        return;

    const messageBox = document.createElement('div');
    messageBox.textContent = message;

    let bgColor, shadowColor;
    if (type === 'red') {
        bgColor = '#dc2626';
        shadowColor = 'rgba(220, 38, 38, 0.3)';
    } else {
        bgColor = 'var(--color-primary)';
        shadowColor = 'rgba(22, 163, 74, 0.3)';
    }

    messageBox.style.cssText = `
        background-color: ${bgColor};
        color: white;
        padding: 10px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px ${shadowColor};
        margin-top: 10px;
        opacity: 0;
        transition: opacity 0.5s ease;
    `;
    messageBoxContainer.appendChild(messageBox);


    setTimeout(() => {
        messageBox.style.opacity = '1';
    }, 10);


    setTimeout(() => {
        messageBox.style.opacity = '0';
        messageBox.addEventListener('transitionend', () => messageBox.remove());
    }, 3000);
}



document.addEventListener('DOMContentLoaded', () => {

    updateStepIndicator();
});

window.nextStep = nextStep;
window.prevStep = prevStep;

let checkOutJs = [];
let totalPrice = 0;

async function loadCheckOutSummry() {
    const response = await fetch("http://localhost:8080/InkSpire/ViewCheckoutSummery");

    if (response.ok) {
        const json = await response.json();

        const itemsContainer = document.getElementById("check-out-item-list");

        itemsContainer.innerHTML = "";
         
        checkOutJs = [];
       
        json.forEach((item, index) => {

            if (item.title && item.price) {
                const itemDiv = document.createElement("div");
                itemDiv.classList.add("check-out-item");

                itemDiv.innerHTML = `
          <img
            src="${item.img}"
            alt="${item.title}"
            class="check-out-item-img"
          />
          <div class="check-out-item-details">
            <h4 class="check-out-item-name">${item.title}</h4>
            <p style="display: none" >${item.listingId}</p>
            <p class="check-out-item-price">Rs<strong>${item.price}</strong>/=</p>
          </div>
        `;
                itemsContainer.appendChild(itemDiv);
                
                checkOutJs.push(item.listingId);
                
            }

             totalPrice = 0;

            if (item.total) {
                const summeryDiv = document.createElement("div");
                summeryDiv.classList.add("check-out-summary");
                const total = parseFloat(item.total) || 0;
                const shipping = parseFloat(item.shipping) || 0;
                totalPrice += total + shipping;


                summeryDiv.innerHTML = `
          <h3>Summery</h3>
          <p>Total:Rs<strong>${item.total}</strong>/= </p>
          <p>Shipping:Rs <strong>${item.shipping}</strong>/= </p>
           
        `;
                itemsContainer.appendChild(summeryDiv);
            }

            document.getElementById("total-price").textContent = totalPrice + ".00 /=";

        });


    } else {
        console.log("Error fetching checkout summary");
    }
}

async function loadUserData() {

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

                    document.getElementById("firstName").value = json.user?.firstName || "First Name not Found";
                    document.getElementById("lastName").value = json.user?.lastName || "Last Name not Found";

                    document.getElementById("email").value = json.user?.email || "E-Mail not Found";
                    document.getElementById("contact").value = json.user?.mobile || "Mobile not Found";


                } else {
                    alert("Please Log In Again!");
                }


            } else {
                alert(json.message);
            }


        } else {
            alert("Network Error Please Try again later!");
        }


    } else {
        alert("an Error Occured Please Log In Again");
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

                    document.getElementById("address1").value = json.address?.line1 || "Street Not Found";
                    document.getElementById("address2").value = json.address?.line2 || "Line 2 Not Found";
                    document.getElementById("addressId").value = json.address?.id || "Null";

                    const citySelect = document.getElementById("city");
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

                    document.getElementById("zipCode").value = json.address?.postal || "Postal Code Not Found";

                } else {
                    alert("Please Log In Again!");
                }


            } else {
                alert(json.message);
            }


        } else {
            alert("Network Error Please try again Later");
        }


    } else {
        alert("Account Error Please Log In  again");
    }

}

async function pay() {
    
        let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    const dataJson = JSON.stringify({
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        address: document.getElementById("addressId").value,
        total:totalPrice,
        listing:checkOutJs,
        user:userId
    });

   console.log(dataJson);
 

      try {
        const res = await fetch('http://localhost:8080/InkSpire/api/payhere/hash', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: dataJson
        });

        const data = await res.json();

        if (!window.payhere) {
          alert("PayHere SDK not loaded");
          return;
        }

        window.payhere.onCompleted = (orderId) => {
          alert("Payment Completed! Order: " + orderId);
          window.location.href = data.return_url + "?order_id=" + orderId;
        };

        window.payhere.onDismissed = () => alert("Payment Cancelled");

        window.payhere.onError = (err) => alert("Payment Error: " + err);

        window.payhere.startPayment(data);

      } catch (err) {
        console.error("Error starting payment:", err);
        alert("Something went wrong. Check console for details.");
      }



}
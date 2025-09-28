
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
window.removeFromCart = removeFromCart;


async function loadCheckOutSummry() {
    const response = await fetch("http://localhost:8080/InkSpire/ViewCheckoutSummery");

    if (response.ok) {
        const json = await response.json();

        const itemsContainer = document.getElementById("check-out-item-list");
        itemsContainer.innerHTML = "";

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
            <p class="check-out-item-price">Rs ${item.price} /=</p>
            <button class="check-out-remove-btn" onclick="removeFromCart(${index})">
              Remove
            </button>
          </div>
        `;
                itemsContainer.appendChild(itemDiv);
            }

            let totalPrice = 0;

            if (item.total) {
                const summeryDiv = document.createElement("div");
                summeryDiv.classList.add("check-out-summary");
                const total = parseFloat(item.total) || 0;
                const shipping = parseFloat(item.shipping) || 0;
                totalPrice += total + shipping;


                summeryDiv.innerHTML = `
          <h3>Summery</h3>
          <p><strong>Total:</strong> Rs ${item.total}/= </p>
          <p><strong>Shipping:</strong> Rs ${item.shipping}/= </p>
           
        `;
                itemsContainer.appendChild(summeryDiv);
            }

            document.getElementById("total-price").textContent = totalPrice +".00 /=";

        });
    } else {
        console.log("Error fetching checkout summary");
    }
}

// auto load
window.onload = loadCheckOutSummry;

// dummy remove function
function removeFromCart(id) {
    alert("Remove item " + id);
}
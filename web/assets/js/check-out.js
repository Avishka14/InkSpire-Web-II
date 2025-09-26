
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
        
        if (!stepStatus) continue;

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

    if (step < 1 || step > totalSteps) return;


    stepForms.forEach(form => {
        if (form) form.classList.add('hidden');
    });
    
    const formToShow = stepForms[step - 1];
    if (formToShow) {
        formToShow.classList.remove('hidden');
        currentStep = step;
        updateStepIndicator();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

function removeFromCart(productId) {
    console.log(`Removed item with ID: ${productId}`);
    alertMessage(`Item ${productId} removed from cart! (Mock action)`, 'red');
}

function alertMessage(message, type = 'blue') {
    const messageBoxContainer = document.getElementById('custom-message-box');
    if (!messageBoxContainer) return;

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


    setTimeout(() => { messageBox.style.opacity = '1'; }, 10);
    

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

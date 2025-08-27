
document.write(`
  <div id="loader">
    <div class="spinner"></div>
    <div class="loading-text">Loading...</div>
  </div>
`);

const style = document.createElement("style");
style.textContent = `
  #loader {
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: #fff;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    z-index: 9999;
    transition: opacity 0.5s ease, visibility 0.5s ease;
  }
  #loader.hidden { opacity: 0; visibility: hidden; }

  .spinner {
    width: 60px; height: 60px;
    border: 6px solid #e6f7f1;
    border-top: 6px solid #0ca371;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 20px;
  }
  @keyframes spin { to { transform: rotate(360deg); } }

  .loading-text {
    font-size: 1.2rem;
    font-weight: 600;
    color: #0ca371;
  }
`;
document.head.appendChild(style);


window.addEventListener("load", () => {
  const loader = document.getElementById("loader");
  loader.classList.add("hidden");
  setTimeout(() => loader.remove(), 500); 
});




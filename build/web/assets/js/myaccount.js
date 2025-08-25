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

}



const sidebarItems = document.querySelectorAll(".sidebar li");
const tabPanels = document.querySelectorAll(".tab-panel");

sidebarItems.forEach(item => {
  item.addEventListener("click", () => {
    sidebarItems.forEach(i => i.classList.remove("active"));
    tabPanels.forEach(panel => panel.classList.remove("active"));
    item.classList.add("active");
    const tabId = item.getAttribute("data-tab");
    document.getElementById(tabId).classList.add("active");
  });
});


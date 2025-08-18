async function loadHtml() {
      try {
        const response = await fetch('Header.html');
        if (!response.ok) {
          throw new Error('Failed to load HTML content');
        }
    
        const htmlContent = await response.text();
    
        document.getElementById('header-div').innerHTML = htmlContent;
      } catch (error) {
        console.error('Error loading HTML:', error);
      }
    }
    
  function sideNav(){
      const navToggle = document.getElementById("navToggle");
      const sideMenu = document.getElementById("sideMenu");
      const overlay = document.getElementById("overlay");
      const closeBtn = document.getElementById("closeMenu");

      if (!navToggle || !sideMenu || !overlay || !closeBtn) {
        console.error("One or more elements not found:", {
          navToggle,
          sideMenu,
          overlay,
          closeBtn
        });
      }

      function openMenu() {
        sideMenu.classList.add("open");
        overlay.classList.add("show");
        sideMenu.setAttribute("aria-hidden", "false");
        overlay.setAttribute("aria-hidden", "false");
        navToggle.setAttribute("aria-expanded", "true");
        document.body.style.overflow = "hidden";
      }

      function closeMenu() {
        sideMenu.classList.remove("open");
        overlay.classList.remove("show");
        sideMenu.setAttribute("aria-hidden", "true");
        overlay.setAttribute("aria-hidden", "true");
        navToggle.setAttribute("aria-expanded", "false");
        document.body.style.overflow = "";
      }

      navToggle.addEventListener("click", openMenu);
      closeBtn.addEventListener("click", closeMenu);
      overlay.addEventListener("click", closeMenu);

      document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") closeMenu();
      });
  }  
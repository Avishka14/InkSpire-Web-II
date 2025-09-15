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




async function loadUserDataFields(){
    
    let userId = document.cookie 
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];
  
    if(userId){

        const response = await fetch (`http://localhost:8080/InkSpire/ReturnUserData?id=${encodeURIComponent(userId)}`);
        
        if(response.ok){
            
            const json = await response.json();
            
            if(json.status){
                
               
                if(json.user){
                     console.log(json.user);
                     
                     document.getElementById("fName").value = json.user?.firstName || "First Name not Found";
                     document.getElementById("Lname").value = json.user?.lastName || "Last Name not Found";
                   
                     let dateOb = new Date(json.user.regDate);
                     let formatted = dateOb.toISOString().split("T")[0];
                    
                    document.getElementById("regDate").value = formatted || "Date not Found";

                    document.getElementById("email").value = json.user?.email || "E-Mail not Found";
                   

                     
                }else{
                     $("#notify").notify("Please Log In Again!", "error");  
                }
                
                
            }else{
                 $("#notify").notify(json.message, "error");  
            }
            
            
        }else{
           $("#notify").notify("Network Error Please Try again later!", "error");  
        }

        
    }else{
         $("#notify").notify("Please Log In Again!", "error");
    }
 
}
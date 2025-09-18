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


async function updateUserBaseInfo(){
    
       let userId = document.cookie 
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];
  
    if(userId){
    
    const data = JSON.stringify({
       firstName : document.getElementById("fName").value, 
       laststName:  document.getElementById("Lname").value
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
    
     if(response.ok){
         
         const json =  await response.json();
         
         if(json.status){
             $(".sv-btn").notify( json.message, "success");
             loadUserDataFields();
         }else{
               $(".sv-btn").notify( json.message, "error");
         }

         
     }else{
            $(".section").notify("Metwork Error Please try again Later", "error");
     }
     
       }else{
         $(".section").notify("Please Log In Again!", "error");
    }
    
}

async function updateUserEmail(){
    
       let userId = document.cookie 
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];
  
    if(userId){
    
    const data = JSON.stringify({
       email : document.getElementById("email").value
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
    
     if(response.ok){
         
         const json =  await response.json();
         
         if(json.status){
             $(".e-btn").notify( json.message, "success");
             loadUserDataFields();
         }else{
               $(".e-btn").notify( json.message, "error");
         }

         
     }else{
            $(".section").notify("Metwork Error Please try again Later", "error");
     }
     
       }else{
         $(".section").notify("Please Log In Again!", "error");
    }
    
}
async function signUp(){
    
     const data = JSON.stringify({
         fname:document.getElementById("firstName").value,
         lname:document.getElementById("last").value,
         email:document.getElementById("email").value,
         password:document.getElementById("password").value
     });
     
    
    const response = await fetch(
              "http://localhost:8080/InkSpire/SignUp",
            {
                method: "POST",
                body: data,
                header: {
                    "Content-Type": "application/json"
                }
            }
            
            );
    
    if(response.ok){
         const json = await response.json();
        
        if(json.status){
            
              window.location.href = "http://localhost:8080/InkSpire/home/home.html";
        }else{
             $.notify(json.message, "error");
        }
  
    
    }else{
         $.notify("ERROR OCCURED !", "error");
    }
    
    
    
}


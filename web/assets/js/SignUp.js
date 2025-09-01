async function signUp(){
    
     const data = JSON.stringify({
         fname:document.getElementById("firstName").value,
         lname:document.getElementById("last").value,
         email:document.getElementById("email").value,
         password:document.getElementById("password").value
     });
     
     console.log(data);
    
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
              console.log("success");
        }else{
             $.notify(json.message, "error");
        }
  
    
    }else{
         $.notify("ERROR OCCURED !", "error");
    }
    
    
    
}


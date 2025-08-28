function showPasswordSection() {
    document.getElementById("get-code-section").style.display = "none";
    document.getElementById("password-section").style.display = "block";
}


async function sendMail() {

    const data = {
        email: document.getElementById("email").value
    };

    const mailAddress = JSON.stringify(data);

    console.log(mailAddress);

    const response = await fetch("http://localhost:8080/InkSpire/MailServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: mailAddress
    });

    if (response.ok) {
        const result = await response.json();

        if (result.status) {
            
            $.notify("Code Has send to your email!", "success");
            
        } else {
            $.notify("Invalid E-Mail Address", "error");
        }
    } else {
        $.notify("Something went wrong Please try again Later", "error");
    }

}

async function verifyCode(){
    
    
    const userCode = document.getElementById("code").value;
    
    console.log(userCode);
    
    const response = await fetch("http://localhost:8080/InkSpire/VerifyCodeServlet", {
        
        method:"POST",
        headers:{
            "Content-Type":"application/x-www-form-urlencoded"
        },
        body:"userCode=" + encodeURIComponent(userCode),
        credentials :"include"
    });
    
    const result = await response.text();
    
    if(result === "Verification successful!"){
     document.getElementById("get-code-section").style.display = "none";
     document.getElementById("password-section").style.display = "block";
   
    }else{
         $.notify("Verification Code is Invalid !", "error");
    }
    
    
}


async function updateUserPassword(){
        
    const data = {
        newPassword: document.getElementById("new-password").value,
        confirmPassword:document.getElementById("confirm-password").value
    };
    
    const dataJson = JSON.stringify(data);
    
    const response = await fetch("http://localhost:8080/InkSpire/PasswordChange", {
           method:"POST",      
           header: {
                "Content-Type": "application/json"
           },
          body:dataJson
    });
    
    if(response.ok){
        const json = await response.json();
        
        if(json.status){
             $.notify(json.message, "success");
       
        }else{
              $.notify(json.message, "error");
              console.log("error" +json.message);
        }
        
        
    }
    
    
}
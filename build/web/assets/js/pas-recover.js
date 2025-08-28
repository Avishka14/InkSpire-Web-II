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
//            document.getElementById("get-code-section").style.display = "none";
//            document.getElementById("password-section").style.display = "block";
            
            $.notify("Code Has send to your email!", "success");
            
        } else {
            $.notify("Something went wrong Please try again Later", "error");
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
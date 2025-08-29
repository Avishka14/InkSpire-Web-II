async function LogIn() {
      
    const data = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };

    const dataToJson = JSON.stringify(data);

    const response = await fetch(
            "http://localhost:8080/InkSpire/LogIn",
            {
                method: "POST",
                body: dataToJson,
                header: {
                    "Content-Type": "application/json"
                }
            }
    );


    if (response.ok) {
        const json = await response.json();

        if (json.status) {

            if (json.message === "200") {
             
             window.location = "http://localhost:8080/InkSpire/home/home.html";
             
            }else if(json.message === "600"){
                
             window.location = "http://localhost:8080/InkSpire/account/adminpanel.html";
             
            } else {
                $.notify("Something went wrong Please try again Later", "error");
            }


        } else {

            $.notify(json.message, "error");
        }



    } else {
         $.notify("ERROR OCCURED !", "error");
    }


}

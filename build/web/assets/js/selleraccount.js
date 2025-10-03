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


async function loadHeadFoot() {
    loadSection("Header.html", "header-area");
    loadSection("footer.html", "footer-area");

}

// Sidebar tab switcher
document.querySelectorAll(".sidebar li").forEach(tab=>{
  tab.addEventListener("click",()=>{
    document.querySelectorAll(".sidebar li").forEach(li=>li.classList.remove("active"));
    tab.classList.add("active");
    let target = tab.dataset.tab;
    document.querySelectorAll(".tab-panel").forEach(panel=>panel.classList.remove("active"));
    document.getElementById(target).classList.add("active");
  });
});

// Messaging functionality
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendMessage");
const messagesArea = document.getElementById("messagesArea");

function sendMessage() {
  const text = messageInput.value.trim();
  if (text === "") return;
  
  // create message element
  const msg = document.createElement("div");
  msg.classList.add("message", "sent");
  msg.textContent = text;
  messagesArea.appendChild(msg);

  // auto-scroll down
  messagesArea.scrollTop = messagesArea.scrollHeight;

  // clear input
  messageInput.value = "";
}

sendBtn.addEventListener("click", sendMessage);
messageInput.addEventListener("keypress", e => {
  if (e.key === "Enter") {
    e.preventDefault();
    sendMessage();
  }
});

async function sendMail() {

    const data = {
        email: document.getElementById("email-seller").value
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

            $(".grey-button").notify("Code Has send to your email!", "success");

        } else {
            $(".grey-button").notify("Invalid E-Mail Address", "error");
        }
    } else {
        $(".grey-button").notify("Something went wrong Please try again Later", "error");
    }

}

async function verifyCode() {


    const userCode = document.getElementById("code").value;

    console.log(userCode);

    const response = await fetch("http://localhost:8080/InkSpire/VerifyCodeServlet", {

        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "userCode=" + encodeURIComponent(userCode),
        credentials: "include"
    });

    const result = await response.text();

    if (result === "Verification successful!") {
        document.getElementById("get-code").style.display = "none";
        document.getElementById("seller-activate-section").style.display = "block";

    } else {
        $(".btncode").notify("Verification Code is Invalid !", "error");
    }


}

async function activateSellerAccount() {

    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const data = JSON.stringify({
            username: document.getElementById("username").value
        });

        const response = await fetch(
                `http://localhost:8080/InkSpire/VerifySellerAccount?id=${encodeURIComponent(userId)}`,
                {
                    method: "POST",
                    body: data,
                    header: {
                        "Content-Type": "application/json"
                    }
                }

        );

        if (response.ok) {

            const json = await response.json();

            if (json.status) {
                $(".btnac").notify("Seller Account Acivated Successfully", "success");

            } else {
                $(".btnac").notify(json.message, "error");
            }


        } else {
            $(".btnac").notify("Network Error Please try again Later", "error");
        }

    } else {
        $(".btnac").notify("Please Log In Again!", "error");
    }


}


async function loadSellerInfo() {


    let userId = document.cookie
            .split("; ")
            .find(row => row.startsWith("userId"))
            ?.split("=")[1];

    if (userId) {

        const response = await fetch(`http://localhost:8080/InkSpire/LoadSellerBaseInfo?id=${encodeURIComponent(userId)}`);

        if (response.ok) {

            const json = await response.json();

            if (json.status) {

                if (json.seller) {
                    console.log(json.seller);
                    document.getElementById("account-layout").style.display = "flex";
                    document.getElementById("seller-profile-not-layout").style.display = "none";

                } else {
                    document.getElementById("account-layout").style.display = "none";
                    document.getElementById("seller-profile-not-layout").style.display = "block";

                }

            } else {
                $(".breadcrumb").notify(json.message, "error");
            }


        } else {
            $(".breadcrumb").notify("Network Error Please try again Later", "error");
        }


    } else {
        $(".breadcrumb").notify("Network Error Please try again Later", "error");
    }

}
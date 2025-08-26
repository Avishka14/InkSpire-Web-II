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

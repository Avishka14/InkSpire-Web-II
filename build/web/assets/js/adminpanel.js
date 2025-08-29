document.addEventListener("DOMContentLoaded", () => {
  const tabs = document.querySelectorAll(".sidebar li");
  const panels = document.querySelectorAll(".tab-panel");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      tab.classList.add("active");

      panels.forEach(p => p.classList.remove("active"));
      document.getElementById(tab.dataset.tab).classList.add("active");
    });
  });
});


async function loadListingData(){
    
    const response = await fetch("http://localhost:8080/InkSpire/LoadListingData");
    
    if(response.ok){
        const json = await response.json();
        
        loadSelect("category" , json.categoryList , "value");
        loadSelect("condition" , json.conditionList , "value");
        
    }else{
        console.log("error");
    }
    

}

function loadSelect(selectId , list , property){
    
    const select = document.getElementById(selectId);
    
    list.forEach(item => {      
       const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);
        
    });
    
}

async function saveProductAdmin(){
    
    const productId = Math.floor(100000 + Math.random() * 900000);
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const categoryId = document.getElementById("category").value;
    const conditionId = document.getElementById("condition").value;
    const itemAvailabilityId = "1";
    const price = document.getElementById("price").value;
    const sellerId = "1" ;
    const approvalId = "1" ;
    
    const image1 = document.getElementById("img1").files[0];
    const image2 = document.getElementById("img2").files[0];
    const image3 = document.getElementById("img3").files[0];
    
    const form = new FormData();
    form.append("productId" , productId);
    form.append("title" , title);
    form.append("description" , description);
    form.append("categoryId" , categoryId);
    form.append("conditionId" , conditionId);
    form.append("itemAvailabilityId" , itemAvailabilityId);
    form.append("sellerId" , sellerId);
    form.append("price" , price);
    form.append("approvalId" , approvalId);
    form.append("image1", image1);
    form.append("image2", image2);
    form.append("image3", image3);
    
   const response = await fetch("http://localhost:8080/InkSpire/AddProduct",{
       method:"POST",
       body:form
   });
   
   if(response.ok){
       
   }
    
    
}
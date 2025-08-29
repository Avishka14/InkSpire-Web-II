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
        
        loadSelect("categoey" , json.categoryList , "value");
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
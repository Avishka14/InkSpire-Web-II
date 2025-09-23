async function  loadSingleView(id) {

    if (id) {

        console.log(id);

        const response = await fetch(`http://localhost:8080/InkSpire/ProductView?id=${id}`);

        if (response.ok) {

            const json = await response.json();

            if (json.product) {

                localStorage.setItem("singleProduct", JSON.stringify(json.product));

                window.location.href = "http://localhost:8080/InkSpire/home/single-product-view.html";

            } else {
                $.notify(json.message, "error");
            }


        } else {
            $.notify("Error in Listings Please try again Later", "error");
        }


    } else {
        $.notify("Network Error Please try again Later", "error");
    }

}




 async function startPayment() {
      try {
        const res = await fetch('http://localhost:8080/InkSpire/api/payhere/hash', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({})
        });

        const data = await res.json();

        if (!window.payhere) {
          alert("PayHere SDK not loaded");
          return;
        }

        window.payhere.onCompleted = (orderId) => {
          alert("Payment Completed! Order: " + orderId);
          window.location.href = data.return_url + "?order_id=" + orderId;
        };

        window.payhere.onDismissed = () => alert("Payment Cancelled");

        window.payhere.onError = (err) => alert("Payment Error: " + err);

        window.payhere.startPayment(data);

      } catch (err) {
        console.error("Error starting payment:", err);
        alert("Something went wrong. Check console for details.");
      }
    }
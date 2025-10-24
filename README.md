# InkSpire — Web

Project Overview

InkSpire is an online book-selling platform built with Java EE (server-side), Hibernate for ORM, MySQL database, and a front-end made with plain HTML, CSS and JavaScript. The project uses a classic Java web app structure and includes Ant build config.

---

## About 
This project is an online book marketplace developed as part of the Web Programming II module. The platform allows users to buy and sell books through an integrated and user-friendly web interface.

Users can browse and purchase books available in the InkSpire Store as well as from other independent sellers who list their books on the platform. Each user has access to a personalized account dashboard where they can:
- View and track their current and past orders
- Check order history and purchase details
- Manage account settings and preferences

In addition to buying, users also have the option to become sellers on the platform. By activating a Seller Account, they can:
- List their own books for sale
- Manage their inventory, prices, and book details
- Monitor sales performance and manage customer orders

The system thus functions as a dual-role marketplace, enabling both buyers and sellers to interact efficiently. It provides a seamless experience for book enthusiasts to purchase books and for independent sellers to reach a wider audience through the InkSpire ecosystem.

---

## Tech Stack

- **Backend** – Java EE (Servlets / JSP / Controllers) + Hibernate (ORM) 
- **Database** – MySQL.
- **Build** – Ant (`build.xml` present in repo).
- **Frontend** – Plain HTML, CSS, JavaScript (files inside `web/`)


**Main Folders**  
- `src/` — Java source code (entities, DAOs, services, controllers)
- `web/` — web resources (HTML/CSS/JS, possibly JSPs)
- `build.xml` — Ant build configuration (used to compile/build the WAR).

---

## Prerequisites

- Java JDK 8+ (project build in JDK 17).
- Apache Ant (since `build.xml` exists).
- Payhere Merchent Account
- Java EE compatible servlet container (Project tested in Glassfish Server)
- MySQL server and a DB
- IDE (developed using Netbeans) (IntelliJ or Eclipse)

---

## Application architecture & main modules

- **Entities / Models:** Hibernate entity classes
- **Repository Layer:** Data access objects using Hibernate sessions or JPA EntityManager to CRUD entities.
- **Service Layer:**  Business logic for catalog operations, user authentication, cart/order processing.
- **Controllers:** HTTP handling (e.g., servlets or MVC controllers) that route requests to services and forward to JSP/HTML pages.
- **Web Layer:** HTML/CSS/JS under web/ (or webapp/WEB-INF/), plus web.xml for servlet mappings.


## Screenshots 

<img width="1439" height="899" alt="Image" src="https://github.com/user-attachments/assets/1aea0938-0949-472e-a853-01793ae1092a" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/8337fba8-1575-42d1-85d1-717e16eb0ca5" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/0c06ef65-a608-4182-a7b1-2c870f0dee34" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/e219fbb5-020e-4fb2-99bd-facc60455d89" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/2261e82b-90ff-47cb-818d-d8a27f366cad" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/ec7d4ece-afa9-4f3c-804c-6bcb863b14b7" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/f09c57ff-4da2-4f1f-ad53-c678c24a7c13" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/e52857aa-5ecd-46dd-a9c9-99fe8123606f" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/0530c47b-528f-47b0-8291-d762cd53b8ee" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/d3306b30-5007-4918-89f3-c2d1aeb5ef10" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/a2a6ec96-ae9f-4f49-b21e-1a6c04aa6dc9" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/1618e13a-0365-4f5c-abc9-8afcb8f2db37" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/e8ab9e4b-b903-4ab3-adf6-eca83c2b8761" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/1c7dcbec-6208-42e7-b654-f48ccd3d9316" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/fa74b6bc-10ff-41bb-9dc2-d5793525996d" />
<img width="1914" height="1079" alt="Image" src="https://github.com/user-attachments/assets/24e41cf3-f158-4547-b4c4-993cd9b19823" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/2c29a929-1ab3-4b3c-bac2-8f51ddbe3aec" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/edf885d0-244a-4e7e-9970-4f3dfabfc412" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/8ac27196-7956-4616-932b-f37d4f495847" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/13033726-8d75-465b-9664-63fce9a16518" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/9ce236af-48b6-432f-ae55-d7efff18fb56" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/59c2239b-8e50-4a5d-9dae-1dc6cbda8f7f" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/f41955c2-c06c-4bde-bfa3-db732ec3d2b2" />
<img width="1919" height="1079" alt="Image" src="https://github.com/user-attachments/assets/4e5f0022-579e-4fa7-8f4e-57d5a7f14215" />

The project is still under development, with ongoing work to implement both buyer and seller functionalities.


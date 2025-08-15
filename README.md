# Hello! 👋

My name is Roxanne Archambault and I am a fourth-year Software Engineering Co-op student at McGill University, graduating in August 2025.

Welcome to my portfolio!

## About me

⚙️ Software Engineering student with 4 years of experience including 5 internships

🔮 Interested in AR/VR graphical application development; current experience in development using Unity Engine and HoloLens2

🔎 Proficient in C#, C++, Java, C, Python, and more

💎 Passionate about using logic and creativity to solve complex problems

🚀 Interested in space exploration and operations,literature, arts, music, and various other topics!

## About this portfolio

I have included here all the major projects I have worked on in my career so far; feel free to go explore them to understand them in more detail! 

### 💫 [**Holoportation**](https://github.com/alex8ndr/Holoportation)

This project, begun as my capstone project in a team of 4 people in Fall 2024 and continued individually as my internship in Summer 2025 with the Canadian Space Agency, aims to reconstruct objects in 3D in real-time and stream this on a HoloLens headset. This is done using Orbbec Femto RGB-D cameras, a C++ and C# Windows application, TCP networking, and a Unity application in C# for the final HoloLens rendering. A document detection feature also leverages OpenCV techniques (and/or a YOLO machine learning model) to detect and segment documents from color frames. Some 3D modelling was also used to model a calibration cube object to merge data from each camera. A [**video demonstration**]() and a [**PDF poster**]() are also included here for a better overview.

### 🦠 [**NucleiSegmentorDetector**]()

This project, completed in a team of 5 people as part of the course ECSE415 Introduction to Computer Vision, applies several computer vision techniques to detect and locate nuclei from medical images in the context of cancer diagnosis and treatment. Unsupervised (k-means clustering, normal cut) and supervised (random forest) learning techniques are used for segmentation, and feature extraction and SVMs are used for detection and counting, all using Python in a Jupyter notebook. The following [**presentation**](https://www.canva.com/design/DAGj0pvUWj4/QvcouV3L4Bz2pZO8vkCcJA/edit?utm_content=DAGj0pvUWj4&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton) gives more details about this.

### 🪡 [**Particle Simulator**](https://github.com/rarchambault/ECSE420-Project)

This project, completed in a team of 4 people as part of the course ECSE420 Parallel Computing, is a simple implementation of a particle simulator in C using OpenGL graphics which was meant to compare sequential and parallel computing techniques for increasing numbers of particles. Sequential execution, CPU threading and GPU threading with NVidia CUDA and OpenCL are the different techniques evaluated; these can be visualized easily by changing an enum variable. The following [**presentation**](https://www.canva.com/design/DAGjaQn522w/CTqXg2F9WV-9F6TfN0z25A/edit?utm_content=DAGjaQn522w&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton) also gives more information about this, including some video demos.

### 🧶 [**PatternCreator**]()

This project allows creating patterns for tapestry crochet or knitting by using a Python script to turn any image into a pixelated image made of squares of K colors, with K being a customizeable variable, and outputting an Excel workbook with one tab containing the image's squares, and a second tab containing each row and the numbers of squares of each color to complete, in order, for each one (like a knitting or crochet pattern). The actual pattern tab is populated by an Excel script. An example input and output are provided in the directory.

### 🧪 [**ECSE429-Project**]()

This project, completed as part of the course ECSE429 Software Validation, is a set of tests in Java meant to validate the performance of a sample TODO list-maker application called "REST API TODO Manager" (the .jar file is included in the directory). Exploratory testing, unit tests and story tests were all implemented to evaluate this application.

### 🚀 [**MUSE Gamescom**]()

This project, completed in a team of 5 European Space Agency professionals, was completed as part of my internship at ESA in Summer 2024 in Cologne for the Gamescom convention as a gamified demonstration meant to the greater public to careers in space. It represents a 3-minute launch and re-entry experience programmed in C with Unreal Engine for Meta Quest 3 where users are placed in a fictional European crewed vehicle which gets launched into space, reaches low-Earth orbit (where some objects start floating around in micro-gravity and can be grabbed and thrown!) and comes back to Earth surrounded by plasma until it splashes down in an ocean. The actual code for this project cannot be shared on this platform, but a [**video demo**]() is provided.

### 🌟 [**HoloMOTS Demo**](https://github.com/rarchambault/rarchambault/tree/main/HoloMOTS%20Demo)

During my internship at the Canadian Space Agency in Fall 2023, I have participated in an intern poster competition which was a presentation to CSA employees to showcase what I did during my internship. Along with my paper poster, I also built a demo application using Unity and C# scripting so that people could try manipulating holograms with the HoloLens2 headset. I cannot provide the code for this application not for the real application I was working on, but I have included in this directory a [**recording**](https://github.com/rarchambault/rarchambault/blob/main/HoloMOTS%20Demo/AR%20Poster%20Demonstration.mp4) of my demo application along with my presentation taken from the HoloLens2 headset as well as my [**PDF poster**](https://github.com/rarchambault/rarchambault/blob/main/HoloMOTS%20Demo/HoloMOTS%20Poster%20-%20Roxanne%20Archambault.pdf).

### 🤖 [**Engineering Games Machine Competition 2023-2024**](https://github.com/rarchambault/rarchambault/tree/main/Machine2024)

This is a robotics project completed to compete in the Machine challenge of the January 2024 Quebec Engineering Games, which is a robotics challenge to be completed over 4 months and showcased at the Games. I was the co-VP Machine for this edition of the competition, meaning I was in charge of the team of 6-20 people (several people contributed to the project, but only a core of 6 people was allowed to work on the robot at the Games. This year's challenge was to build a Mars rover able to pick up plastic balls and place them in tall silos by crossing a 12' x 8' map under an 8" tall platform. The robot runs using a Raspberry Pi using Python code and sereval DC, stepper and servo motors as well as a vacuum to pick up balls.

**Note**: The [**repository**](https://github.com/MachineMGCIL/2023machine) for this project is not public yet.

### 🎭 [**ESCAPE**](https://github.com/rarchambault/ESCAPE/tree/5987d63d12b7c01f064cbe07d0400a38787f277b)

This web application exposes a REST API interface and uses a Vue.js frontend and a PostgreSQL relational database to provide a website for the McGill Engineering Socials Committee, allowing users to purchase tickets for events and post pictures and messages about them. This was a group project completed in a team of 10 people as part of the course ECSE428 Software Engineering Practice.

### 🏛 [**Museum Software System**](https://github.com/rarchambault/rarchambault/tree/main/Museum%20Software%20System)

This project, completed in a team of 6 people as part of the course ECSE321 Introduction to Sofware Engineering, is a web application exposing a REST API interface and using a Vue.js frontend and a PostgreSQL relational database to provide a management system for museum owners wishing to allow their visitors to purchase tickets, view artwork and take art pieces out on loans.

### 🦆 [**RoboDuckies**](https://github.com/WassimJabz/RoboDuckies/tree/e8931298b853999a711a9c258d94c3ed4b2381a9)

This project, completed in a team of 6 people as part of the course ECSE211 Design Principles and Methods, was a robot using a Raspberry Pi and Lego motors and sensors to place foam cubes into a mosaic following an input pattern.

**Note**: The repository for this project is not public yet.

### 🏊‍♀️ [**DiveSafe**](https://github.com/rarchambault/rarchambault/tree/main/DiveSafe)

This project, completed as part of the course ECSE223 Model-Based Programming in a team of 6 people, is a system meant for managing a resort where people can book diving expeditions with guides, rent equipment and plan their trip.

### 💵 [**McKrillCroesus**](https://github.com/rarchambault/McKrillCroesus/tree/92515c36b338004adab835f31641d464c24b5dad)

This project, completed as part of the 2023 Computer Science Games, was a challenge to optimize stock market transactions based on a given API and given stock prices. This was completed in a team of two and my team won 5th place for this challenge!

### 🏝 [**McKrillWebMobile**](https://github.com/rarchambault/McKrillWebMobile/tree/37378a742dd76b0c4fd735b63865201db3a58b63)

This project was completed as part of the 2023 Computer Science Games where I implemented, in a team of two people, to implement a web application with a given API. This web application runs on C# and Maui and provides weather information for a paradise island.

### 🏓 [**Pong Game**](https://github.com/rarchambault/pong-game/tree/a6492840e369a51ea266919d5d78d3f7fe41b061)

This project, completed as part of the course ECSE202 Introduction 
to Software Development, implements simple Java classes and a graphical application which simulates a ping pong game where the user plays against a virtual opponent and can choose its difficulty level.

## Contact Information

**[Email](mailto:roxanne.archambault@mail.mcgill.ca)** | **[LinkedIn](https://www.linkedin.com/in/roxanne-archambault/)**

<div align="center">
  <h1>MOSI Backend :yellow_heart:</h1>
</div>

<a id="top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h3 align="center"><a href="http://www.mosi.digital/">MOSI</a></h3>
  <p align="center">혼자서 면접 준비하는 취업 준비생들은 본인이 질문에 대한 올바른 답변을 하였는지 확인하는데 어려움이
있습니다. 이를 보완하기 위해 면접자의 음성을 Text로 변환하여 저장하고, GPT API를 활용하여 올바른 대답인지 피드백 받을 수 있게 하는 서비스입니다. 또한 취업 준비생들끼리 정보를 공유하는 커뮤니티 서비스입니다. </p>
  <p align="right">기획: 김지혜</p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#languages-framework-libraries">Languages, Framework, Libraries</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#team-roles">Team Roles</a></li>
    <li><a href="#feature">Feature</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#coming-soon">Coming Soon</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#project-link">Project Link</a></li>
  </ol>
</details>

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- LANGUAGES, FRAMEWORK, LIBRARIES -->
<a id="languages-framework-libraries"></a>
## Languages, Framework, Libraries
#### Languages
* JAVA
    - version: JDK-17

#### Framework
* Spring Boot
    - version: 3.3.0
    - project: Maven

#### Libraries
* SpringBoot data-jpa
    - version: 3.3.0
* SpringBoot starter-web
    - version: 3.3.0
* mysql-connector-j
    - version: 8.3.0
* Lombok
    - version: 1.18.32
* SpringBoot starter-test
    - version: 3.3.0
* querydsl-apt
    - version: 5.1.0
* querydsl-jpa
    - version: 5.1.0
* hibernate-core
    - version: 6.1.0.Final
* jjwt
    - version: 0.12.6
* jjwt-api
    - version: 0.11.5
* amazonaws-s3
    - version: 1.12.765

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- GETTING STARTED -->
<a id="getting-started"></a>
## Getting Started
1. Clone the repo
    ```sh
    git clone https://github.com/Jr-YongFill/backend
    ```
2. Build Project
    ```sh
    cd ./backend
    mvn -B package -DskipTests=true --file pom.xml
    ```
3. Run
    ```sh
    java -jar ./target/server-0.0.1-SNAPSHOT.jar
    ```

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- TEAM ROLES -->
<a id="team-roles"></a>
## Team Roles
- **장희권 (팀장)**
  - Security, QueryDSL_SQL 환경 구축
  - Question, Stack, Vote API 작성
  - Exception 처리

- **김지혜**
  - Post API
  - File Server 구축
  - Logging

- **배창민**
  - JWT Security
  - Member (SignIn, SignOut) API

- **이동규**
  - Entity, Repository, Package 구축
  - Answer Note, Comment API 작성

### Deployment & Planning
- **배포:** 장희권 (CI/CD, Docker 활용)

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- FEATURE -->
<a id="feature"></a>
## Feature
1. Information sharing through the community.
2. GPT API evaluates user responses.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- ROADMAP -->
<a id="roadmap"></a>
## Roadmap
- [ ] Project Setting
- [ ] Member API
- [ ] Auth
- [ ] Vote API
- [ ] Question API
- [ ] Community API
- [ ] Answer API

See the [project issues](https://github.com/Jr-YongFill/backend/issues) for a full list of proposed features and known issues.

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTRIBUTING -->
<a id="contributing"></a>
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement". Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- COMING SOON -->
<a id="coming-soon"></a>
## Coming Soon
- [ ] Realtime Chatting
- [ ] Social Login
- [ ] UI/UX

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTACT -->
<a id="contact"></a>
## Contact
**hg_yellow**
- GitHub: [hg_yellow](https://github.com/jang010505)
- Mail: hgyellow0505@gmail.com
  
**김지혜**
- GitHub: [wisdom](https://github.com/Wisdom-Kim)
- Mail: cocoa389@naver.com

**배창민**
- GitHub: [Changchang](https://github.com/bbmini96)
- Mail: changmin38@gmail.com

**이동규**
- GitHub: [이동규](https://github.com/202011988)
- Mail: dlehdrb5509@naver.com

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- PROJECT LINK -->
<a id="project-link"></a>
## Project Link
- Service: [MOSI 웹사이트](http://www.mosi.digital/)
- Backend: [GitHub Backend Repository](https://github.com/Jr-YongFill/backend)
- Frontend: [GitHub Frontend Repository](https://github.com/Jr-YongFill/frontend)
- Project Team: [Jr.yongfill](https://github.com/Jr-YongFill)

<p align="right">(<a href="#top">back to top</a>)</p>

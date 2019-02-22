[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Korean](https://img.shields.io/badge/language-Korean-blue.svg)](#korean)  
  
OpenGDS / Mobile (공간자료 편집도구)
=======
Version 1.0 February 11th, 2019
![logo](https://user-images.githubusercontent.com/13480171/52541836-c0127180-2ddc-11e9-88ad-27cf0af977c9.png)
  
  
  
  
  
본 솔루션의 이름은 GeoDT Moible이며 소개 홈페이지는 다음과 같습니다. <link>(http://www.geodt.co.kr/pages/mobile.html)

  
  
  [Arbiter 모듈 확장 API.docx](https://github.com/ODTBuilder/Mobile/blob/master/Arbiter%20모듈%20확장%20API.docx)를 참고하여 [OpenGDS](https://github.com/ODTBuilder/Mobile/tree/master/OpenGDS_Android)를 추가하여 API를 적용하세요.
  
  
이 프로젝트는 국토공간정보연구사업 중, [공간정보 SW 활용을 위한 오픈소스 가공기술 개발]과제를 수행하기 위하여 제작되고 있습니다.
Arbiter(<link>https://github.com/ROGUE-JCTD/Arbiter-Android) 안드로이드 오픈 소스를 사용하였습니다. Arbiter는 상황 별 인식 및 지형 공간 데이터 수집에 사용할 지도를 제작할 수 있는 모바일 앱입니다. 지도와 레이어를 다운로드하면 Arbiter를 사용하여 오프라인에서 편집 할 수 있습니다. 3G 또는 4G 셀룰러 네트워크 또는 Wi-Fi를 통해 연결하면 지도 레이어를 서버와 동기화 할 수 있으며 다른 사람들도 업데이트를 수신하게됩니다. 
  
정식 버전은 차후에 통합된 환경에서 제공될 예정입니다.
이 프로그램들은 완성되지 않았으며, 최종 완료 전 까지 문제가 발생할 수도 있습니다.
발생된 문제는 최종 사용자에게 있으며, 완료가 된다면 제시된 라이선스 및 규약을 적용할 예정입니다.

감사합니다.
공간정보기술(주) 연구소 <link>http://www.git.co.kr/
OpenGeoDT 팀

연구기관
---
세부 책임 : 부산대학교 <link>http://www.pusan.ac.kr/

연구 책임 : 국토연구원 <link>http://www.krihs.re.kr/
  
  #
  
  ### 목차
    
  - [기능 소개](#기능-소개)  
    - [기존 기능](#기존-기능)
      - [피처 삽입 / 편집](#피처-삽입-및-편집)
        - [피처 삽입](#피처-삽입)
        - [피처 편집](#피처-편집)
      - [오류 네비게이터](#오류-네비게이터)
    - [추가 기능](#추가-기능)
      - [다국어 설정](#다국어-설정)
      - [주소 검색 기능](#주소-검색-기능)
      - [좌표 검색 기능](#좌표-검색-기능)
      - [베이스 맵 추가](#베이스-맵-추가)
      - [레이어 검수 기능](#레이어-검수-기능)
      - [관심지역 이미지 삽입](#관심지역-이미지-삽입)
  - [요구 사양](#요구-사양)
  - [참고 자료](#참고-자료)
  - [사용 라이브러리](#사용-라이브러리)



기능 소개
=====
   
## 기존 기능
### 피처 삽입 및 편집
#### 피처 삽입
  
  
| <center>피처 삽입 전</center> | <center>피처 삽입 후</center> |
|:--------:|:--------:|
| <img width="700" alt="삽입전" src="https://user-images.githubusercontent.com/13480171/52546402-28277e80-2e02-11e9-8edd-d1da955b4826.png"> |<img width="700" alt="삽입후" src="https://user-images.githubusercontent.com/13480171/52546400-278ee800-2e02-11e9-80d7-6060192a04ea.png"> |
  
  #### 피처 편집
  
| <center>피처 편집 전</center> | <center>피처 편집 후</center> |
|:--------:|:--------:|
| <img width="700" alt="편집전" src="https://user-images.githubusercontent.com/13480171/52546399-278ee800-2e02-11e9-80cf-718b35485bcb.png"> |<img width="700" alt="편집후" src="https://user-images.githubusercontent.com/13480171/52546398-278ee800-2e02-11e9-8a24-4336ddbd3db0.png"> |

  ### 오류 네비게이터
  <img width="700" alt="오류네비게이터" src="https://user-images.githubusercontent.com/13480171/52548570-d71f8680-2e11-11e9-8b1d-c544333e7702.png">
  
## 추가 기능
### 다국어 설정 
  
  
<img width="700" alt="다국어 설정" src="https://user-images.githubusercontent.com/13480171/52542114-d66dfc80-2ddf-11e9-83e2-42b74ca9ce8d.png">
  
<div id="3 4"/>
  
  
### 주소 검색 기능
<img width="700" alt="주소 검색 기능" src="https://user-images.githubusercontent.com/13480171/52542225-fb16a400-2de0-11e9-8f31-98adcacc6ffa.png">
  
  
### 좌표 검색 기능
<img width="700" alt="좌표 검색 기능" src="https://user-images.githubusercontent.com/13480171/52542231-0d90dd80-2de1-11e9-9d85-fe1efd783cae.png">
  
  
### 베이스 맵 추가

| <center>Open Street Map</center> | <center>Bing Road</center> |
|:--------:|:--------:|
| <img width="700" alt="openStreetMap" src="https://user-images.githubusercontent.com/13480171/52542534-0fa86b80-2de4-11e9-9227-fe4a84b1c3c9.png"> |<img width="700" alt="BingRoad" src="https://user-images.githubusercontent.com/13480171/52542536-10410200-2de4-11e9-8ef5-d68c3932b7d2.png"> |
  
| <center>Bing Aerial</center> | <center>Bing Aerial With Labels</center> |
|:--------:|:--------:|
| <img width="700" alt="BingAerial" src="https://user-images.githubusercontent.com/13480171/52542538-12a35c00-2de4-11e9-8106-53c02522db0c.png"> |<img width="700" alt="BingAerialWithLabels" src="https://user-images.githubusercontent.com/13480171/52542539-1b942d80-2de4-11e9-8e01-63e19f052a5f.png"> |
  
  
### 레이어 검수 기능

| <center>검수 옵션</center> | <center>검수 결과</center> |
|:--------:|:--------:|
| <img width="700" alt="검수옵션" src="https://user-images.githubusercontent.com/13480171/52542596-bee54280-2de4-11e9-99ab-430fff20a971.png"> |<img width="700" alt="검수결과" src="https://user-images.githubusercontent.com/13480171/52542595-be4cac00-2de4-11e9-9a64-db28f73df122.png"> |
  
  -검수에 대한 자세한 설명은 [사용자 매뉴얼](https://github.com/ODTBuilder/Mobile/blob/master/GeoDT_Mobile%20v1.0%20사용자%20매뉴얼.pdf)의 3.9 검수 작업을 참고하십시오.
  
  
### 관심지역 이미지 삽입
  
| <center>이미지 삽입 전</center> | <center>이미지 삽입 후</center> |
|:--------:|:--------:|
| <img width="700" alt="삽입전" src="https://user-images.githubusercontent.com/13480171/52547050-acc8cb80-2e07-11e9-8754-6fa8c9fa91bd.png"> |<img width="700" alt="삽입후" src="https://user-images.githubusercontent.com/13480171/52547049-acc8cb80-2e07-11e9-9c44-52c5b8c5dd22.png"> |

  #

- 이상 확장된 기능에 대하여 자세한 참고사항은 [Arbiter 모듈 확장 API.docx](https://github.com/ODTBuilder/Mobile/blob/master/Arbiter%20모듈%20확장%20API.docx) 을. 참고하십시오.
  #  
  
요구 사양
=====
### 1. 환경 ###
- Android version 4.1(Jelly Bean) 이상
- Android SDK Minimum Version = 11, Android SDK Target Version = 19

### 2. 설치 ###
- 프로젝트 빌드
- apk 설치
- 솔루션 설치 및 서버 구동([OpenGDSBuilder2018Prod](https://github.com/ODTBuilder/OpenGDSBuilder2018Prod)와 [OpenGDSBuilder2018Cons](https://github.com/ODTBuilder/OpenGDSBuilder2018Cons))

참고 자료
=====
- Arbiter-Android(기존, 확장) : 기존 / 확장 기능설명
- Manual
- Arbiter 모듈 확장

사용 라이브러리
=====
1. Arbiter-Android (MIT License) <link>https://github.com/ROGUE-JCTD/Arbiter-Android
2. Android SDK <link>https://www.android.com
3. Cordova <link>https://cordova.apache.org
4. GSON <link>https://github.com/google/gson
5. JQuery <link>https://jquery.com
6. OpenLayers <link>https://openlayers.org
7. Proj4js <link>http://proj4js.org

OpenGDS / Mobile (Geospatial Information Editor)
=======

The project of 'Land Geospatial Information Research and Development, Korea', has been designated to carry out the [Development of Open Source Geospatial Software] challenges.

This project used Arbiter Android Open Source. The full version will be available in the future, an integrated environment.
These programs have not been completed, the final version can cause problems to the full.
The problem is to end-users, if completed, will be applied to the proposed license and conventions.


OpenSource Team. R&D dept, Geospatial Information Technology Co.Ltd.

Research Group
---
Group Leader : Pusan National University <link>http://www.pusan.ac.kr/

Research Leader : Korea Research Institute for Human Settlements <link>http://www.krihs.re.kr/

Extended Functions
=====

1. Image Overlay
2. Address Search
3. Coordinate Search
4. Multilingual(English, Korean, Portuguese, Spanish)
5. Additional Base Map
6. Layer Validation

Libraries
=====

1. Arbiter-Android (MIT License) <link>https://github.com/ROGUE-JCTD/Arbiter-Android


Mail
====
Developer : Seulgi, Lee
ghre55@git.co.kr

🎬 YouTube Tools

A powerful toolkit to generate video tags, extract thumbnails, and fetch detailed YouTube video information.

📌 Overview

YouTube Tools is a Java/Spring Boot–based web application that helps YouTubers and content creators improve their workflow.
It provides:

✔️ Auto-generated tags for any YouTube video

✔️ Suggested related video tags

✔️ Downloadable thumbnails in all qualities

✔️ Full video details (title, channel, publish date, views, etc.)

This tool uses the YouTube Data API v3 and provides a clean UI to simplify content optimization.

✨ Features
🔖 Tag Generation

Generates SEO-friendly tags for any YouTube video

Extracts tags from related videos

Helps boost video ranking

🖼️ Thumbnail Tools

Fetch & download thumbnails in SD, HD, and Max Resolution

Instant URL-based extraction

📊 Video Details

Get complete details including:

Title

Description

Channel Name

Publish Date

Views, Likes, Comments

Category & Video Type

🌐 API Powered

Built using Spring WebClient

Fast & lightweight architecture

🧰 Tech Stack

Java 24

Spring Boot 4

YouTube Data API v3

Maven

Thymeleaf / HTML CSS JS (if applicable)

📂 Project Structure
/src
 ├── main
 │   ├── java/com/youtubetools
 │   │    ├── controller
 │   │    ├── service
 │   │    ├── model
 │   │    └── config
 │   └── resources
 │        ├── templates
 │        ├── static
 │        └── application.properties
└── test

⚙️ Setup Instructions
1️⃣ Clone the repository
git clone https://github.com/your-username/youtube-tools.git
cd youtube-tools

2️⃣ Add YouTube API Key

In application.properties:

youtube.api.key=YOUR_API_KEY
youtube.api.base.url=https://www.googleapis.com/youtube/v3

3️⃣ Run the project
mvn spring-boot:run

📌 API Endpoints
Method	Endpoint	Description
GET	/thumbnail	Opens thumbnail extraction page
POST	/thumbnail/download	Downloads YouTube thumbnail
GET	/api/tags/{videoId}	Fetches tags of a video
GET	/api/related-tags/{videoId}	Fetches tags from related videos
GET	/api/video-details/{videoId}	Full video details
🖼️ Example Outputs
✔️ Tags
["technology", "spring boot", "api", "youtube tutorials", ...]

✔️ Thumbnail URLs
https://img.youtube.com/vi/VIDEO_ID/maxresdefault.jpg

✔️ Video Details (JSON)
{
  "title": "How to Use YouTube API",
  "channel": "Tech Creator",
  "views": 152399,
  "uploaded": "2023-10-14"
}

🤝 Contributing

Contributions, issues, and feature requests are welcome!

📜 License

MIT License

👨‍💻 Author

Nishant Kumar
Computer Science Student – VIT Bhopal
Backend Developer (Java | Spring Boot)

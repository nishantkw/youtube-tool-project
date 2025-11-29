package com.youtubetools.controller;

import com.youtubetools.model.VideoDetails;
import com.youtubetools.service.thumbnailservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.youtubetools.service.YouTubeService;

@Controller
@RequiredArgsConstructor
public class YouTubeVideoController {

    private final YouTubeService youTubeService;
    private final thumbnailservice service;

    // ------------------------
    // Show Video Details Page
    // ------------------------
    @GetMapping("/youtube/video-details")
    public String showVideoForm() {
        return "video-details";   // thymeleaf page name
    }

    // ------------------------
    // Process Video URL / ID
    // ------------------------
    @PostMapping("/youtube/video-details")
    public String fetchVideoDetails(@RequestParam String videoUrlOrId, Model model) {

        // Extract video ID
        String videoId = service.extractVideoId(videoUrlOrId);

        if (videoId == null) {
            model.addAttribute("error", "Invalid YouTube URL or ID.");
            return "video-details";
        }

        // Fetch details from service
        VideoDetails details = youTubeService.getVideoDetails(videoId);

        if (details == null) {
            model.addAttribute("error", "Video not found.");
        } else {
            model.addAttribute("videoDetails", details);
        }

        model.addAttribute("videoUrlOrId", videoUrlOrId);
        return "video-details";
    }


}

package com.youtubetools.controller;

import com.youtubetools.service.thumbnailservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class thumbnailcontroller {

    @Autowired
    private thumbnailservice service; // same class name, injected

    @GetMapping("/thumbnail")
    public String getThumbnail() {
        return "thumbnails"; // returns the view
    }

    @PostMapping("/get-thumbnail")
    public String showThumbnail(@RequestParam("videoUrlOrId") String videoUrlOrId, Model model) {
        String videoId = service.extractVideoId(videoUrlOrId); // extract ID
        if (videoId == null) {
            model.addAttribute("error", "Invalid YouTube URL");
            return "thumbnails";
        }

        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg";
        model.addAttribute("thumbnailUrl", thumbnailUrl);
        return "thumbnails";
    }
}

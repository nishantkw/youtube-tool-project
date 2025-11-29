package com.youtubetools.service;

import com.youtubetools.model.SearchVideo;
import com.youtubetools.model.Video;
import com.youtubetools.model.VideoDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final WebClient.Builder webClientBuilder;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelatedVideos;

    // --------------------------------------------------
    // MAIN METHOD: Search Videos
    // --------------------------------------------------
    public SearchVideo searchVideos(String videoTitle) {

        List<String> videoIds = searchForVideoIds(videoTitle);

        if (videoIds.isEmpty()) {
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideos(Collections.emptyList())
                    .build();
        }

        String primaryVideoId = videoIds.get(0);

        List<String> relatedIds = videoIds.subList(
                1,
                Math.min(videoIds.size(), maxRelatedVideos + 1)
        );

        Video primaryVideo = getVideoById(primaryVideoId);

        List<Video> relatedVideos = new ArrayList<>();
        for (String id : relatedIds) {
            Video v = getVideoById(id);
            if (v != null) relatedVideos.add(v);
        }

        return SearchVideo.builder()
                .primaryVideo(primaryVideo)
                .relatedVideos(relatedVideos)
                .build();
    }

    // --------------------------------------------------
    // STEP 1: Search API -> return video IDs
    // --------------------------------------------------
    private List<String> searchForVideoIds(String videoTitle) {

        SearchApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", videoTitle)
                        .queryParam("type", "video")
                        .queryParam("maxResults", maxRelatedVideos + 1)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(SearchApiResponse.class)
                .block();

        if (response == null || response.items == null)
            return Collections.emptyList();

        List<String> ids = new ArrayList<>();
        for (SearchItem item : response.items) {
            if (item.id != null && item.id.videoId != null)
                ids.add(item.id.videoId);
        }

        return ids;
    }

    // --------------------------------------------------
    // GET FULL VIDEO DETAILS (used separately in UI)
    // --------------------------------------------------
    public VideoDetails getVideoDetails(String videoId) {

        VideoApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.getItems().isEmpty()) {
            return null;
        }

        Snippet snippet = response.getItems().get(0).getSnippet();
        String thumbnailUrl = snippet.getThumbnails().getBestThumbnailUrl();

        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.getTitle())
                .description(snippet.getDescription())
                .tags(snippet.getTags() == null ? Collections.emptyList() : snippet.getTags())
                .thumbnailUrl(thumbnailUrl)
                .channelTitle(snippet.getChannelTitle())
                .publishedAt(snippet.getPublishedAt())
                .build();
    }

    // --------------------------------------------------
    // INTERNAL: Basic Video Info fetch
    // --------------------------------------------------
    private Video getVideoById(String videoId) {

        VideoApiResponse response = webClientBuilder.baseUrl(baseUrl).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items == null || response.items.isEmpty())
            return null;

        Snippet s = response.items.get(0).snippet;

        return Video.builder()
                .id(videoId)
                .channelTitle(s.channelTitle)
                .title(s.title)
                .tags(s.tags == null ? Collections.emptyList() : s.tags)
                .build();
    }

    // --------------------------------------------------
    // INTERNAL API RESPONSE MODELS
    // --------------------------------------------------

    @Data
    static class SearchApiResponse {
        List<SearchItem> items;
    }

    @Data
    static class SearchItem {
        Id id;
    }

    @Data
    static class Id {
        String videoId;
    }

    @Data
    static class VideoApiResponse {
        List<VideoItem> items;
    }

    @Data
    static class VideoItem {
        Snippet snippet;
    }

    @Data
    static class Snippet {
        String title;
        String description;
        String channelTitle;
        String publishedAt;
        List<String> tags;
        Thumbnails thumbnails;
    }

    // ------------------------------
    // THUMBNAILS SUBOBJECT
    // ------------------------------
    @Data
    static class Thumbnails {
        Thumbnail high;
        Thumbnail medium;
        Thumbnail standard;
        Thumbnail maxres;

        public String getBestThumbnailUrl() {
            if (maxres != null) return maxres.url;
            if (high != null) return high.url;
            if (medium != null) return medium.url;
            if (standard != null) return standard.url;
            return null;
        }
    }

    @Data
    static class Thumbnail {
        String url;
        int width;
        int height;
    }
}

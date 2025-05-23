package com.example.studynotebackend.controller;

import com.example.studynotebackend.domain.Note;
import com.example.studynotebackend.dto.NoteDto;
import com.example.studynotebackend.mapper.UserMapper;
import com.example.studynotebackend.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserMapper userMapper;

    private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public NoteController(NoteService noteService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword
    ) {
        List<Note> notes = noteService.getNotes(page, size, keyword);
        List<NoteDto> dtos = notes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> get(@PathVariable Long id) {
        Note note = noteService.getById(id);
        NoteDto dto = toDto(note);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) {
        Note created = noteService.create(note);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(
            @PathVariable Long id,
            @RequestBody Note note
    ) {
        Note updated = noteService.update(id, note.getTitle(), note.getContent());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private NoteDto toDto(Note note) {
        NoteDto dto = new NoteDto();
        dto.setId(note.getId());
        dto.setUserId(note.getUserId());
        // 从用户表查用户名
        String username = userMapper.findById(note.getUserId()).getUsername();
        dto.setAuthorName(username);
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt().format(F));
        dto.setUpdatedAt(note.getUpdatedAt().format(F));
        return dto;
    }
}

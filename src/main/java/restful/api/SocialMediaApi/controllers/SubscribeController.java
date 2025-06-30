package restful.api.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import restful.api.SocialMediaApi.dto.SubscribeDTO;
import restful.api.SocialMediaApi.services.SubscribeService;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
@RequiredArgsConstructor
@Tag(name = "Подписки", description = "Управление подписками")
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping()
    @Operation(summary = "Возвращает все подписки", description = "Выводит из БД все подписки, возвращая информацию о пользователях, состоящих в этой подписке")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<SubscribeDTO>> index(){
        return ResponseEntity.ok(subscribeService.findAllSubscribes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Возвращает все подписки на указанного пользователя", description = "Выводит из БД все подписки на указанного пользователя, по ID в пути, возвращая информацию о пользователях, состоящих в этой подписке")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<List<SubscribeDTO>> show(@PathVariable Long id){
        return ResponseEntity.ok(subscribeService.findSubscribesByUserId(id));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Создает новую подписку", description = "Создает подписку от авторизованного пользователя к указанному по ID")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> add(@PathVariable Long id){
        return ResponseEntity.ok(subscribeService.createSubscribe(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаляет подписку", description = "Удаляет подписку от авторизованного пользователя к указанному по ID")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<SubscribeDTO> delete(@PathVariable Long id){
        return ResponseEntity.ok(subscribeService.deleteSubscribe(id));
    }
}

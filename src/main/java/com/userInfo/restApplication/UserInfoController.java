package com.userInfo.restApplication;

import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@Validated
public class UserInfoController {

    // /wishitemsを参考に、あえてパラメータは小文字で設定。
    // @GetMapping("/userinfo)の値「names」を「name」に変えるとデフォルトバリューしか表示されなくなるため「names」と設定。
    // アップデートの場合、値の変更だから本来はsetterを使うべきなのかな。
    // 日付表現は、より良い方法が色々ありそうだった。
    // バリデーションを機能させるためには「Validation」dependencyを追加しなければならない。
    // フィールドに「final」修飾子を付ける場合はコンストラクタを作成する必要がある。つまり、コンストラクタのために定数化するということかな。

    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, String>> getUsers(@RequestParam(value = "names", defaultValue = "noname") String names,
                                                        @Pattern(regexp = "^(19[2-9][0-9]|20[0-2][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String birthdays) {
        return ResponseEntity.ok(Map.of("names", names, "birthday", birthdays));
    }

    @GetMapping("/username")
    public ResponseEntity<Map<String, String>> getNames(@RequestParam(value = "name", defaultValue = "username") @Length(max = 20) String name) {
        return ResponseEntity.ok(Map.of("username", name));
    }

    @GetMapping("/birthday")
    public ResponseEntity<Map<String, String>> getBirthdays(@RequestParam(value = "name", defaultValue = "username")
                                                                @Pattern(regexp = "^(19[2-9][0-9]|20[0-2][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$") String birthday) {
        return ResponseEntity.ok(Map.of("birthday", birthday));
    }

    @PostMapping("/newuser")
    public ResponseEntity<Map<String, String>> create(@RequestBody @Validated CreateForm form) {
        String name = form.getName();
        String birthday = form.getBirthday();
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/names/id")
                .build()
                .toUri();
        return ResponseEntity.ok(Map.of("name successfully created", name, "birthday successfully created", birthday));
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable("id") int id, @RequestBody @Validated UpdateForm form) {
        String name = form.getName();
        String birthday =form.getBirthday();
        return ResponseEntity.ok(Map.of("name successfully updated", name, "birthday successfully updated", birthday));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok(Map.of("user", "user successfully deleted"));
    }
}

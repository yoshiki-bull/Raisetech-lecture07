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
@RequestMapping("/users")
public class UsersController {

    // /wishitemsを参考に、あえてパラメータは小文字で設定。
    // @GetMapping("/userinfo)の値「names」を「name」に変えるとデフォルトバリューしか表示されなくなるため「names」と設定。
    // アップデートの場合、値の変更だから本来はsetterを使うべきなのかな。
    // 日付表現は、より良い方法が色々ありそうだった。
    // バリデーションを機能させるためには「Validation」dependencyを追加しなければならない。
    // フィールドに「final」修飾子を付ける場合はコンストラクタを作成する必要がある。つまり、コンストラクタのために定数化するということかな。
    // 「境界値テスト（境界値分析）」は、境界値を狙ってテストする技法。
    // 「@RequestMapping」は、クラスとクラスパスを紐づけるアノテーション。（優秀！）
    // GET(取得)の時は、Validationが機能しない。
    // エンドポイントの名詞は一貫して「複数形」を使う。(単複変換で混乱しないように)
    // 「final」-属性の値は一定でなければならない！

    private final String birthdayPa = "^(19[2-9][0-9]|20[0-2][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";

    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getUsers(@RequestParam(value = "names", defaultValue = "noname") String names,
                                                        @Validated @Pattern(regexp = birthdayPa) String birthdays) {
        return ResponseEntity.ok(Map.of("names", names, "birthday", birthdays));
    }

    @RequestMapping(value = "/usernames", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getNames(@RequestParam(value = "name", defaultValue = "username")
                                                            @Validated @Length(max = 20) String name) {
        return ResponseEntity.ok(Map.of("username", name));
    }

    @RequestMapping(value = "/birthdays", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getBirthdays(@RequestParam(value = "name", defaultValue = "username")
                                                                @Validated @Pattern(regexp = birthdayPa) String birthday) {
        return ResponseEntity.ok(Map.of("birthday", birthday));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> create(@RequestBody @Validated CreateForm form, UriComponentsBuilder uriBuilder) {
        String name = form.getName();
        String birthday = form.getBirthday();
        Map<String,String> message = Map.of("name", name, "birthday,", birthday);
        URI url = uriBuilder.path("/id" + "/" + name + "/" + birthday)
                .build()
                .toUri();
        return ResponseEntity.created(url).body(message);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, String>> update(@PathVariable("id") int id, @RequestBody @Validated UpdateForm form) {
        String name = form.getName();
        String birthday =form.getBirthday();
        return ResponseEntity.ok(Map.of("message", "successfully updated", "name", name,
                                        "birthday", birthday));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        return ResponseEntity.noContent().build();
    }
}

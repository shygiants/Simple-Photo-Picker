# Simple Photo Picker

Simple Photo Picker는 간단하게 Android 기기의 사진들을 보여주고 선택하도록 하는 라이브러리입니다.

<img src="https://github.com/shygiants/Simple-Photo-Picker/blob/master/static/demo1.png" alt="Pick single photo" width="45%" height="auto">
<img src="https://github.com/shygiants/Simple-Photo-Picker/blob/master/static/demo2.png" alt="Pick multiple photos" width="45%" height="auto">

## Quick Start

* `PhotoPickerActivity`를 시작하는 `Intent`를 생성합니다.
* 그 `Intent`에 `PhotoPickerActivity.ARG_IS_MULTIPLE`을 key로 multiple 옵션을 설정합니다. (default는 single)
* `onActivityResult()`에서 `PhotoPickerResultResolver`를 이용하여 `PhotoPickerActivity`의 결과를 처리합니다.



### 사진 여러 개 고르기

```java
public class ExampleActivity extends Activity {
  private final static int REQ_PICK_PHOTOS = 1;

  void startPhotoPickerActivity() {
    Intent intent = new Intent(this, PhotoPickerActivity.class);
    intent.putExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, true);
    startActivityForResult(intent, REQ_PICK_PHOTOS);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQ_PICK_PHOTOS && resultCode == RESULT_OK)
      List<Photo> photosPicked = PhotoPickerResultResolver.resolve(data);
  }
}
```

### 사진 하나 고르기

```java
public class ExampleActivity extends Activity {
  private final static int REQ_PICK_PHOTO = 1;

  void startPhotoPickerActivity() {
    Intent intent = new Intent(this, PhotoPickerActivity.class);
    intent.putExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, false);
    startActivityForResult(intent, REQ_PICK_PHOTO);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQ_PICK_PHOTO && resultCode == RESULT_OK)
      Photo photoPicked = PhotoPickerResultResolver.resolve(data).get(0);
  }
}
```

### Photo 클래스

```java
ImageView imageView = (ImageView) findViewById(R.id.image_view);
ImageView thumbnailView = (ImageView) findViewById(R.id.thumbnail_view);

// onActivityResult()에서 Photo 객체를 얻었다고 가정합니다.
Photo photoPicked = PhotoPickerResultResolver.resolve(data).get(0);
Uri imageUri = photoPicked.getImageUri();
Uri thumbnailUri = photoPicked.getThumbnailUri();

// ImageView 설정
imageView.setImageURI(imageUri);
thumbnailView.setImageURI(thumbnailUri);

// Uri에서 File 객체 생성
File file = new File(imageUri.getPath());

```

자세한 사용법은 [Sample](https://github.com/shygiants/Simple-Photo-Picker/tree/master/sample)을 참고해주세요.

## Download
[ ![Download](https://api.bintray.com/packages/shygiants/maven/simple-photo-picker/images/download.svg) ](https://bintray.com/shygiants/maven/simple-photo-picker/_latestVersion) 또는

### Gradle

```groovy
compile 'io.github.shygiants:simple-photo-picker:1.0.2'
```

Simple Photo Picker는 최소 Android 5.0.1 Lollipop(API 21)을 요구합니다.

## Credits

* [airpair 블로그](https://www.airpair.com/android/Photo-Gallery-Android-Studio-List-Fragments)

## License

    Copyright 2016 Sanghun Yun

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

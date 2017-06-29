<?php
  header("Content-Type: text/html; charset=UTF-8");
  function send_notification ($tokens, $message) {
    $url = 'https://fcm.googleapis.com/fcm/send';
    $GOOGLE_API_KEY = 'AIzaSyBOrSycnZnBMZAdet-3EqtxEM4Qaffbdz8';

    $fields = array (
      'registration_ids' => $tokens,
      'data' => $message
    );

    $headers = array (
      'Authorization:key = ' . $GOOGLE_API_KEY,
      'Content-Type: application/json'
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    $result = curl_exec($ch);

    if ($result === FALSE) {
      die('Curl failed: ' . curl_error($ch));
    }

    curl_close($ch);

    return $result;
  }

  $hostname = "localhost";
  $database = "mattmatt96";
  $username = "mattmatt96";
  $password = "tlawogns96";
  $connect = mysqli_connect($hostname, $username, $password, $database);
  if (!$connect) {
    die('데이터베이스 접근에 문제가 있습니다.' . mysqli_error($connect));
  }

  mysqli_set_charset($connect, "utf8");

  mysqli_select_db($connect, "Member");
  $query_mysql_push="SELECT Token FROM Member";
  $tokens = array();

  if ($result_push = mysqli_query($connect, $query_mysql_push)) {
    if(mysqli_num_rows($result_push) > 0) {
      while ($row_push = mysqli_fetch_assoc($result_push)) {
        $tokens[] = $row_push["Token"];
      }
    }
  }

  mysqli_close($connect);

  $myMessage = $_GET['message']; //폼에서 입력한 메세지를 받음

  if ($myMessage == ""){
    $myMessage = "안녕하세요!";
  }

  $message = array("message" => $myMessage);
  $message_status = send_notification($tokens, $message);

  echo $message_status;

?>

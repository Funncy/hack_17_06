<?php
  header("Content-Type: text/html; charset=UTF-8");

  $hostname = "localhost";
  $database = "mattmatt96";
  $username = "mattmatt96";
  $password = "tlawogns96";
  $connect = mysqli_connect($hostname, $username, $password, $database);
  if (!$connect) {
    die('데이터베이스 접근에 문제가 있습니다.' . mysqli_error($connect));
  }

  mysqli_set_charset($connect, "utf8");

  $prokey = mysqli_real_escape_string($connect, $_POST['key']);

  if (empty($prokey)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else {
    mysqli_select_db($connect, "Project");
    $query_mysql="SELECT * FROM Project WHERE ProKey = BINARY('$prokey')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      if ($number > 0) {
        while($row = mysqli_fetch_assoc($result)){
          echo $row['ProKey'];
          echo ",";
          echo $row['ProName'];
          echo ",";
          $leader = $row['ProLeader'];
          echo $leader;
          echo ",";
          echo $row['ProIntroduce'];
          echo ",";
        }
      } else {
        echo "empty";
      }
    } else {
      $json_data = array (
        "result" => "Unauthorized(3)"
      );

      echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
    }

    mysqli_select_db($connect, "ProMembers");
    $query_mysql="SELECT * FROM ProMembers WHERE ProKey = BINARY('$prokey')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      echo $number-1;
      echo ",";
      if ($number > 0) {
        while($row = mysqli_fetch_assoc($result)){
          if ($leader != $row['MemID']) {
           echo $row['MemID'];
           echo ",";
         }
        }
      } else {
        echo "empty";
      }
    } else {
      $json_data = array (
        "result" => "Unauthorized(4)"
      );

      echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
    }

  }
  mysqli_close($connect);
?>

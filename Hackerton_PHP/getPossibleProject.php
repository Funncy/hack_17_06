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

  $sno = mysqli_real_escape_string($connect, $_GET['id']);

  if (empty($sno)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else { // 접근 완료
    mysqli_select_db($connect, "Member");
    $query_mysql="SELECT * FROM Member WHERE ID = BINARY('$sno')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $ro = mysqli_fetch_assoc($result);
      if ($ro['AndroidFlag']) {
        $andriod = 1;
      }
      if ($ro['WebFlag']) {
        $web = 1;
      }
      if ($ro['IOSFlag']) {
        $ios = 1;
      }
      if ($ro['DesignFlag']) {
        $design = 1;
      }
    }

    mysqli_select_db($connect, "Project");
    $query_mysql="SELECT * FROM Project WHERE ";
    if ($result = mysqli_query($connect, $query_mysql)) {
      $number = mysqli_num_rows($result);
      echo $number;
      echo ",";
      if ($number > 0) {
        while($row = mysqli_fetch_assoc($result)){
           echo $row['ProKey'];
           echo ",";
           echo $row['ProName'];
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
  }

  mysqli_close($connect);
?>

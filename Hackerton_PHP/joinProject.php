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

  $id = mysqli_real_escape_string($connect, $_POST['id']);
  $memid = mysqli_real_escape_string($connect, $_POST['memid']);

  if (empty($id)) {
    $json_data = array (
      "result" => "Unauthorized(1)"
    );

    echo json_encode($json_data, JSON_UNESCAPED_UNICODE);
  } else { // 접근 완료
    mysqli_select_db($connect, "Project");
    $query_mysql="SELECT * FROM Project WHERE ProLeader = Binary('$id')";
    if ($result = mysqli_query($connect, $query_mysql)) {
      if (mysqli_num_rows($result) > 0) {
        $result_each = mysqli_fetch_assoc($result);

        $projectKey = $result_each['ProKey'];
      } else {
        echo "empty";
      }
    }

    mysqli_select_db($connect, "ProMembers");
    $query_mysql="SELECT * FROM ProMembers WHERE ProKey = '" . $projectKey . "' and MemID = '" . $memid . "'";
    if ($result = mysqli_query($connect, $query_mysql)) {
      mysqli_select_db($connect, "ProMembers");
      $query_mysql="INSERT INTO ProMembers(Memid, ProKey) VALUES('$memid', '$projectKey')";
      if ($result = mysqli_query($connect, $query_mysql)) {
        echo "Authorized";
      } else {
        echo "Unauthorized";
      }
    }
  }

  mysqli_close($connect);
?>

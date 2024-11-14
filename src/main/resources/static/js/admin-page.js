$(window).on('load', function () {
  $.ajax({
    url: '/admin/users',
    method: 'GET',
    success: function (data) {
      $('#data').html(data);
    }
  });

  const manage_user = document.getElementById('manage-user');
  const manage_board = document.getElementById('manage-board');
  const manage_goods = document.getElementById('manage-goods');
  const manage_post = document.getElementById('manage-post');

  manage_user.addEventListener('click', function () {
    $.ajax({
      url: '/admin/users',
      method: 'GET',
      success: function (data) {
        $('#data').html(data);
      },
      error: function (xhr, status, error) {
        $('#data').html('Error: ' + error);
      }
    });
  });

  manage_board.addEventListener('click', function () {
    $.ajax({
      url: '/admin/board',
      method: 'GET',
      success: function (data) {
        $('#data').html(data);
      },
      error: function (xhr, status, error) {
        $('#data').html('Error: ' + error);
      }
    });
  });

  manage_goods.addEventListener('click', function () {
    $.ajax({
      url: '/admin/goods',
      method: 'GET',
      success: function (data) {
        $('#data').html(data);
      },
      error: function (xhr, status, error) {
        $('#data').html('Error: ' + error);
      }
    });
  })

  manage_post.addEventListener('click', function () {
    $.ajax({
      url: '/admin/posts',
      method: 'GET',
      success: function (data) {
        $('#data').html(data);
      },
      error: function (xhr, status, error) {
        $('#data').html('Error: ' + error);
      }
    });
  })
});
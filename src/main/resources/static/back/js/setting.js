function modUser(user){
    // 获取<meta>标签中封装的_csrf信息
    var token = $("meta[name='_csrf']").attr("content");
    var title = '修改用户信息';
    var resultTitle = '修改成功';
    var url = 'modUser'
    var flag = true;
    var confirmText = '确认修改'
    if (user==null) {
        flag = false
        url = 'addUser'
        title = '添加用户信息'
        resultTitle = '添加成功'
        confirmText = '确认添加'
        user = {
            username:"",
            password:"",
            email:""
        }
    }
    swal({
        title: title,
        input: 'select',
        inputOptions: {
            admin:'admin',
            user:'user'
        },
        inputPlaceholder: '选择用户权限',
        html: '<form id="usermod">用户名<input id="swal-input1" class="swal2-input" type="text" name="username" value="'+ user.username + '">' +
            '密码<input id="swal-input2" class="swal2-input" type="password" name="password" value="' + user.password + '">' +
            '邮箱<input id="swal-input3" class="swal2-input" type="email" name="email" value="' + user.email + '"></form>',
        inputAttributes: {
            autocapitalize: 'off'
        },
        showCancelButton: true,
        confirmButtonText: confirmText,
        showLoaderOnConfirm: true,
        preConfirm: (authority) => {
            var parms = $("#usermod").serialize()
            if (authority === '') {
                authority = 'user'
            }
            return fetch(url,{
                method:'post',
                credentials:"include",
                headers: {
                    "Content-Type":"application/x-www-form-urlencoded",
                    "x-csrf-token":token
                },
                body:!flag?(parms + '&' + 'authority=' + authority):("id=" + user.id + '&' + parms + '&' + 'authority=' + authority)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(response.statusText)
                    }
                    return response.json()
                })
                .catch(error => {
                    console.log(error)
                })
        },
        allowOutsideClick: () => !swal.isLoading()
    }).then((result) => {
        if (result.success) {
            swal({
                title: resultTitle,
                type:'success',
                showLoaderOnConfirm: true,
            }).then(()=>{
                window.location.reload();
            })

        }
    })
}

function delUser(id) {
    // 获取<meta>标签中封装的_csrf信息
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    if(confirm('确定删除该用户吗?')){
        $.ajax({
            type:'post',
            url : '/admin/deleteUser',
            data: {id:id},
            dataType: 'json',
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (result) {
                if (result && result.success) {
                    swal("用户删除成功","success").then(()=>{
                        window.location.reload();
                    })
                    // window.alert("用户删除成功");

                } else {
                    window.alert(result.msg || '用户删除失败')
                }
            }
        });
    }
}
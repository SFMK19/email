const { connect, FileType } = window.ShimoJSSDK;
const fileId = "my-new-file-u98213"; // 刚才定义的文件id

// const { signature, token } = await getCredentialsFromServer() // 从您的后端服务获取用于石墨鉴权的签名和 token
const signature = "{your-calculated-signature}"; // 测试时直接写在这里
const token = "{your-customized-token}"; // 测试时直接写在这里

connect({
    debug: true,
    fileId: fileId,
    endpoint: "https://office.shimoapi.com/sdk/v2",
    signature: signature,
    token: token,
    container: document.querySelector("#iframe-container"), // iframe 挂载的目标容器元素id
}).then((sdk) => {
    // sdk 即为 ShimoSDK 实例
    // console.log(sdk)
});
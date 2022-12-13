<template>
    <div class="mx-15 my-15">
        <v-form ref="form" v-model="valid" lazy-validation>
            <div class="text-h6 mb-5"> Login </div>
            <v-text-field v-model="email" :counter="10" :rules="emailRules" label="E-mail" required></v-text-field>

            <v-text-field v-model="password" :rules="passwordRules" label="Password" type="password" required></v-text-field>

            <v-checkbox v-model="checkbox" label="Remember?"
                required></v-checkbox>

            <v-btn color="success" class="mr-4" @click="validate">
                Login
            </v-btn>

            <v-btn color="success" class="mr-4" @click="testBtn">
                TestBTN
            </v-btn>
        </v-form>
    </div>
</template>


<script>
import axios from 'axios'
export default {
    name: 'Login',
    data: () => ({
        valid: true,
        email: '',
        emailRules: [
            v => !!v || 'กรุณากรอก E-mail',
            // v => /.+@.+\..+/.test(v) || 'กรุณากรอกให้ถูกต้อง',
        ],
        password: '',
        passwordRules: [
            v => !!v || 'กรุณากรอกรหัสผ่าน',
        ],
        select: null,
        checkbox: false,
    }),

    methods: {
        async validate() {
            const { valid } = await this.$refs.form.validate()

            if (valid) this.loginHandler()
            else alert('Something Wrong.')
        },
        reset() {
            this.$refs.form.reset()
        },
        resetValidation() {
            this.$refs.form.resetValidation()
        },
        loginHandler() {

            let loginData = new FormData()
            loginData.append("username", this.email)
            loginData.append("password", this.password)

            axios.post('http://localhost:8080/login', loginData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }).then(function (res) {
                console.log("RES::: ", res);
                if (res) {
                    localStorage.setItem("cookie", res.data.cookie);
                }
            }).catch(function (error) {
                console.log(error);
            })
        },

        async testBtn(){

            let tmp = localStorage.getItem("cookie").split(RegExp("[=;]"))[1];

            console.log(tmp)

            /*

            // axios.create({baseURL: 'http://localhost:8080/'});
            // axios.defaults.headers.common['Cookie'] = "TEST";
            axios.interceptors.request.use(
                async config => {
                    console.log('.... CONFIG ....', config);
                    config.url = "http://localhost:8080/reservations";
                    config.headers = { "Authorization": "" + tmp };
                    return config;
                }
            );
            axios.interceptors.response.use(
                response => {
                    console.log('AXIOS.RES.: ', response);
                }
            );

            axios.get('reservations')
            .then(function (res) {
                console.log("RES::: ", res.data);
            }).catch(function (error) {
                console.log(error);
            })
            // axios.get('http://localhost:8080/reservations'
            // ).then(function (res) {
            //     console.log("RES::: ", res.data);
            // }).catch(function (error) {
            //     console.log(error);
            // })

            */

            axios.get('http://localhost:8080/reservations', {
                withCredentials: true,
                headers: {
                    "Access-Control-Allow-Origin": "localhost",
                    "Cookie": localStorage.getItem("cookie").split(";")[0]
                }
            })
        }
    },

  mounted() {
  }
}
</script>
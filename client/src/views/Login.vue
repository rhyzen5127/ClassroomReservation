<template>
    <div class="mx-15 my-15">
        <v-form ref="form" v-model="valid" lazy-validation>
            <div class="text-h6 mb-5"> Login </div>
            <v-text-field v-model="email" :counter="10" :rules="emailRules" label="E-mail" required @keyup.enter="validate"></v-text-field>

            <v-text-field v-model="password" :rules="passwordRules" label="Password" type="password"
                required @keyup.enter="validate"></v-text-field>

            <v-checkbox v-model="checkbox" label="Remember?" required></v-checkbox>

            <v-btn color="success" class="mr-4" @click="validate">
                Login
            </v-btn>

            <div>
                <v-overlay v-model="loading" class="align-center justify-center">
                    <v-progress-circular size="70" color="orange" indeterminate>
                    </v-progress-circular>
                </v-overlay>
            </div>

            <div>
                <v-overlay v-model="authFailDialog" class="align-center justify-center">
                    <v-card class="my-2 pa-5 text-center">
                        <div class="text-center">
                            <h2 class="text-red">
                                ล้มเหลว
                            </h2>
                            E-mail หรือรหัสผ่านผิดพลาด กรุณาลองใหม่
                        </div>
                        <v-btn color="black" class="mt-5" @click="confirmDialog = false, successDialog = false, authFailDialog = false">
                            <h5> ปิด </h5>
                        </v-btn>
                    </v-card>

                </v-overlay>
            </div>

        </v-form>
    </div>
</template>


<script>
import axios from 'axios'
import { useUserStore } from '@/stores/users.js'
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
        loading: false,
        authFailDialog: false,
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
            this.loading = true

            let loginData = new FormData()
            loginData.append("username", this.email)
            loginData.append("password", this.password)


            this.userStore.login(this.email, this.password).then((res) => {
                console.log("RES::: ", res);
                if (res) {
                    localStorage.setItem("cookie", res.token);
                    localStorage.setItem("email", res.user.email);
                    localStorage.setItem("fname", res.user.firstName);
                    localStorage.setItem("lname", res.user.lastName);
                    localStorage.setItem("role", res.user.role);
                    this.loading = false
                    window.location.href = '/'
                }
                // REDIRECT to HOME
            }).catch(err => {
                this.loading = false
                this.authFailDialog = true
            })
        },
    },

    setup() {
        return {
            userStore: useUserStore()
        }
    },

    mounted() {
    }
}
</script>
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

            <v-btn color="primary" class="mr-4" @click="loginAsStaffTest()">
                Login as STAFF
            </v-btn>

            <v-btn color="secondary" class="mr-4" @click="loginAsUserTest()">
                Login as USER
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
        loginAsStaffTest(){
            this.email = 'admin'
            this.password = 'voraphat'
            this.loginHandler()
        },

        loginAsUserTest(){
            this.email = 'user@gmail.com'
            this.password = 'voraphat'
            this.loginHandler()
        },

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
            this.userStore.login(this.email, this.password).then((res) => {
                console.log("RES::: ", res);
                localStorage.setItem("cookie", res)
                // REDIRECT to HOME
                
                window.location.href = '/'
            }).catch(err => {
                localStorage.setItem("cookie", null)
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
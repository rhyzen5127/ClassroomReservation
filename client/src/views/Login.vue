<template>
    <div class="mx-15 my-15">
        <v-form ref="form" v-model="valid" lazy-validation>
            <div class="text-h6 mb-5"> Login </div>
            <v-text-field v-model="email" :counter="10" :rules="emailRules" label="E-mail" required></v-text-field>

            <v-text-field v-model="password" :rules="passwordRules" label="Password" required></v-text-field>

            <v-checkbox v-model="checkbox" label="Remember?"
                required></v-checkbox>

            <v-btn color="success" class="mr-4" @click="validate">
                Login
            </v-btn>
        </v-form>
    </div>
</template>


<script>
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

            if (valid) alert('Form is valid')
        },
        reset() {
            this.$refs.form.reset()
        },
        resetValidation() {
            this.$refs.form.resetValidation()
        },
    },
}
</script>
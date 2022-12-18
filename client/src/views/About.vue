<template>
  <div class="text-center align-center justify-center ma-15">
    <v-img class="text-center" :src="logoPath" max-height="150" /><br />
    <h3>
    โครงการ เว็บไซต์จองห้องเรียนสำหรับบุคลากรภายในคณะวิทยาศาสตร์<br /><br />

    รายวิชา<br />
    05506017 SOFTWARE ENGINEERING <br /><br />

    เสนอ ผศ.ดร.วรางคณา กิ้มปาน<br /><br />

    จัดทำโดย<br />
    นายวรภัทร อัศวธงชัย 63050182<br />
    นายสหชัย แผลงฤทธิ์ 63050197<br />
    นายนิธิศ วงษ์กวน 63050150<br />
    นายภูธเนศ ประสิทธิ์สิน 63050169<br />
    นายภูวสุ ชูมณี 63050172<br />
    นายวรพัทธ์ ตฤณเกศโกศล 63050181<br />
    นายวริทธิ์นันท์ ทิพย์โภชนา 63050184 <br /><br />

    คณะวิทยาศาสตร์ สาขาวิทยาการคอมพิวเตอร์<br />
    สถาบันเทคโนโลยีพระจอมเกล้าเจ้าคุณทหารลาดกระบัง<br />
  </h3>
  </div>
</template>

<script>
export default {
  data: () => ({
    logoPath: new URL("/titleLogo.png", import.meta.url).href,
  }),

  components: {
  }
}
</script>
<template>
  <!-- <div class="text-center align-center justify-center ma-15">
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

  <hr class="my-16" />-->

  <div class="mt-15 mx-15">
    <div class="text-h4 text-green text-center">วิธีการใช้งาน</div>
    <div class="text-h5 my-5 text-cyan">1. ผู้ใช้ทั่วไปที่ไม่มีบัญชีผู้ใช้ของเว็บไซต์</div>
    <v-card class="text-h6 text-center my-5 mx-16 mt-6" color="pink"
      >หนัาหลัก</v-card
    >
    <!-- pic1.1 -->
    <v-img class="text-center" :src="pic1" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 1.1 ภาพแสดงหน้าหลัก เมื่อไม่ได้ Login
    </div>
    <div class="my-5">
      อธิบายเมนูของหน้าหลัก ดังนี้ <br />
      <ul class="mx-5">
        <li><b>หมายเลข 1</b> ปุ่ม Log in เมื่อคลิกจะไปยังภาพที่ 1.2</li>
        <li>
          <b>หมายเลข 2</b>
          ตารางปฏิทินเป็นปฏิทินที่สามารถคลิกเลือกวันใดวันหนึ่งหรือคลิกค้างเพื่อลากคลุมหลาย
          ๆ วันได้ เมื่อเลือกแล้วจะแสดงห้องเรียนและเวลาที่ถูกจองได้ทางด้านขวา
        </li>
        <li>
          <b>หมายเลข 3</b> ตัวกรอง
          ใช้กรองเลือกอาคารและห้องเรียนที่จะแสดงทางด้านขวาได้
        </li>
        <li>
          <b>หมายเลข 4</b> sidebar เป็นแถบเมนูที่จะแสดงฟังก์ชันต่าง ๆ เช่น
          หน้าหลักโดยจะแตกต่างกันตามการ - Login ว่าไม่ได้ Login, เป็น User
          หรือเป็น Staff
        </li>
      </ul>
    </div>
  </div>

  <v-divider class="mx-12 mt-5 mb-8" />

  <div class="mx-15">
    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้า Login</v-card
    >
    <div class="my-5 mx-5">
      - เมื่อคลิกเลือกหมายเลข 1 ในภาพ 1.1 จะแสดงหน้านี้ <br />
    </div>
    <!-- pic1.2 -->
    <v-img class="text-center" :src="pic4" max-height="500" /><br />

    <div class="text-caption text-center">ภาพที่ 1.2 ภาพแสดงหน้า Login</div>
    <div class="my-5 mx-5">
      - เมื่อเข้าสู่หน้า Login จะมีช่องให้ใส่ E-mail และ Password
      เมื่อกรอกข้อมูลครบ ปุ่ม Login จะขึ้นให้สามารถกดได้ หากข้อมูลที่กรอกผิดพลาด
      จะขึ้นแจ้งเตือนว่า <br />
    </div>
    <!-- pic1.2.1 -->
    <v-img class="text-center" :src="pic2" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 1.2.1 ภาพแสดงหน้า Login ล้มเหลว
    </div>
    <div class="my-5 mx-5">
      - แต่หากกรอกข้อมูล E-mail ไม่ถูกประเภท จะขึ้นแจ้งเตือนว่า <br />
    </div>
    <!-- pic1.2.2 -->
    <v-img class="text-center" :src="pic3" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 1.2.2 ภาพแสดงหน้า Login E-mail กรอก E-mail ไม่ถูกประเภท
    </div>
  </div>

  <v-divider class="mx-12 mt-5 mb-8" />

  <div class="mx-15">
    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้าค้นหาและจองห้องเรียน</v-card
    >
    <div class="my-5 mx-5">
      - เมื่อคลิกเลือกที่ “ค้นหาและจองห้องเรียน” ในหมายเลข 4 ภาพ 1.1 <br />
    </div>
    <!-- pic1.3 -->
    <v-img class="text-center" :src="pic5" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 1.3 ภาพหน้าค้นหาและจองห้องเรียน เมื่อไม่ได้ Login
    </div>
    <ul class="my-5 mx-5">
      <li>
        <b>หมายเลข 1</b> ตัวเลือกอาคารและห้องเรียน
        ใช้เลือกอาคารและห้องเรียนที่ต้องการดูสถานะ โดยจะต้องเลือกอาคารก่อน
        จึงจะเลือกห้องเรียนได้
      </li>
      <li>
        <b>หมายเลข 2</b> ตัวเลือกวันที่ และเวลาที่ต้องการดูสถานะ
        จะต้องเลือกทั้งเวลาเริ่มต้นและเวลาสิ้นสุด
      </li>
      <li>
        <b>หมายเลข 3</b> แถบแสดงสถานะห้องเรียน จะต้องใส่ข้อมูลทั้งหมายเลข 1 และ
        2 จึงจะแสดงได้
      </li>
    </ul>
  </div>

  <hr class="my-16" />

  <div class="mx-15">
    <div class="text-h5 my-5 text-cyan">2. ผู้ใช้ที่มีบัญชีผู้ใช้ในระดับ User</div>
    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หนัาหลัก</v-card
    >
    <div class="my-5 mx-5">
      - เมื่อ Login ด้วย account ที่เป็น User จะแสดงหน้านี้ <br />
    </div>
    <!-- pic2.1 -->
    <v-img class="text-center" :src="pic6" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 2.1 ภาพแสดงหน้าหลัก เมื่อ Login เป็น User
    </div>
    <div class="my-5 mx-5">
      - หน้าหลัก Login เป็น User นั้นจะแตกต่างจากภาพที่ 1.1 ตรง “ประวัติการจอง”
      ในหมายเลข 4 ภาพที่ 1.1 <br />
    </div>


    <v-divider class="mx-12 mt-5 mb-8" />

    
    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้าค้นหาและจองห้องเรียน</v-card
    >
    <!-- pic2.2 -->
    <v-img class="text-center" :src="pic7" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 2.2 ภาพแสดงหน้าค้นหาและจองห้องเรียน เมื่อ Login เป็น User
    </div>
    <div class="my-5 mx-5">
      - ในภาพนี้ จะคล้ายกับภาพที่ 1.3 แต่เพิ่มในส่วนหมายเลข 1 และ หมายเลข 2
      <br />
      <ul class="mx-5">
        <li>
          <b>หมายเลข 1</b> รายละเอียดการจอง รับรายละเอียดการจองเพื่อส่งไปให้
          Staff ทราบก่อนอนุมัติ
        </li>
        <li><b>หมายเลข 2</b> ปุ่มจองห้อง จะคลิกได้เมื่อกรอกข้อมูลครบ</li>
      </ul>
    </div>

    <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้าประวัติการจอง</v-card
    >
    <!-- pic2.3 -->
    <v-img class="text-center" :src="pic8" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 2.3 ภาพแสดงหน้าประวัติการจอง
      จะสามารถดูประวัติการจองห้องได้และสามารถกดยกเลิกการจองของตัวที่ยังไม่มีการอนุมัติได้
    </div>
  </div>

  <hr class="my-16" />

  <div class="mx-15">
    <div class="text-h5 my-5 text-cyan">3. ผู้ดูแลที่มีบัญชีผู้ใช้ในระดับ Staff</div>
    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หนัาหลัก</v-card
    >
    <!-- pic3.1 -->
    <v-img class="text-center" :src="pic9" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 3.1 ภาพแสดงหน้าหลัก เมื่อ Login เป็น Staff
    </div>
    <div class="my-5 mx-5">
      - หน้าหลัก Login เป็น User นั้นจะแตกต่างจากภาพที่ 1.1 ตรง “ประวัติการจอง”,
      “อนุมัติคำขอจอง”, “แก้ไขรายละเอียดห้องเรียน” และ “ส่งออกรายงานการใช้ห้อง”
      ในหมายเลข 4 ภาพที่ 1.1
    </div>

    <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้าค้นหาและจองห้องเรียน</v-card
    >
    <!-- pic3.2 -->
    <v-img class="text-center" :src="pic10" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 3.2 ภาพแสดงหน้าค้นหาและจองห้องเรียน เมื่อ Login เป็น Staff
    </div>
    <div class="my-5 mx-5">
      - ในขั้นตอนค้นหาและจองห้องเรียนจะเหมือนกับภาพที่ 2.2
      แต่ตอนจองห้องจะไม่ต้องรออนุมัติจากStaff จะจองทันทีที่กดจอง
    </div>

    <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >ประวัติการจอง</v-card
    >
    <!-- pic3.3 -->
    <v-img class="text-center" :src="pic11" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 3.3 ภาพแสดงหน้าประวัติการจอง
      จะสามารถดูประวัติการจองห้องได้และสามารถกดยกเลิกการจองของตัวที่ยังไม่มีการ
      อนุมัติได้
    </div>


    <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >อนุมัติการจอง</v-card
    >
    <!-- pic3.4 -->
    <v-img class="text-center" :src="pic12" max-height="500" /><br />

    <div class="text-caption text-center">ภาพที่ 3.3 ภาพหน้าอนุมัติการจอง</div>
    <div class="my-5 mx-5">
      - เป็นหน้าสำหรับการอนุมัติการจองห้องเรียนของ User
      <br />
      <ul class="mx-5">
        <li>
          <b>หมายเลข 1</b> เป็นปุ่มอนุมัติและไม่อนุมัติของการจองห้องเรียน
        </li>
        <li>
          <b>หมายเลข 2</b> หมายเหตุที่จะส่งไปยัง User ทราบ
        </li>
      </ul>
      สามารถกรอกหรือไม่กรอกก็ได้
    </div>

  <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >หน้าแก้ไขรายละเอียดห้องเรียน</v-card
    >
    <!-- pic3.5 -->
    <v-img class="text-center" :src="pic13" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 3.5 ภาพหน้าแก้ไขรายละเอียดห้องเรียน
    </div>
    <ul class="my-5 mx-5">
      <li>
        <b>หมายเลข 1</b> ตัวเลือกอาคารและห้องเรียน
        ใช้เลือกอาคารและห้องเรียนที่ต้องการดูสถานะโดยจะต้องเลือกอาคารก่อน
        จึงจะเลือกห้องเรียนได้ และต้องเลือกให้ครบก่อนหมายเลข 2, 3
        จึงจะแสดงออกมา
      </li>
      <li>
        <b>หมายเลข 2</b> รายละเอียดของห้องเรียน สามารถแก้ไขรายละเอียด
        จำนวนที่นั่ง และสถานะพร้อมใช้งานได้
        โดยจำนวนที่นั่งจะต้องเป็นจำนวนเต็มบวกหรือ 0 เท่านั้น <br />
      </li>
      <li>
        <b>หมายเลข 3</b> ปุ่มยืนยันและยกเลิก
        เป็นส่วนใช้กดยืนยันหรือยกเลิกการแก้ไขห้องโดยกดที่ปุ่ม ๆ นั้น
      </li>
    </ul>

    <v-divider class="mx-12 mt-5 mb-8" />

    <v-card class="text-h6 text-center my-5 mx-16" color="pink"
      >ส่งออกรายงานการใช้ห้อง</v-card
    >
    <!-- pic3.6 -->
    <v-img class="text-center" :src="pic14" max-height="500" /><br />

    <div class="text-caption text-center">
      ภาพที่ 3.6 ภาพหน้าส่งออกรายงานการใช้ห้อง
    </div>
    <div class="my-5 mx-5">
      - จะมีลักษณะคล้ายกับภาพที่ 3.1 ที่ย่อส่วนลง แล้วเพิ่มหมายเลข 1, 2 และ 3
      ด้านล่างแทน
      <ul class="mx-5">
        <li>
          <b>หมายเลข 1</b> รายการที่จะส่งออก แสดงถึงรายละเอียดการจองทั้ง อาคาร
          ห้อง ผู้จอง อีเมลผู้จอง รายละเอียด วันที่ เวลา และหมายเหตุ
        </li>
        <li>
          <b>หมายเลข 2</b> ส่งออก จะทำการส่งออกรายงานโดยจะเป็นไฟล์ .pdf
          ได้มากสุดที่ 15 แถว
        </li>
        <li>
          <b>หมายเลข 3</b> รีเซ็ต ทำการเริ่มต้นรายงานใหม่
        </li>
      </ul>
      <br />
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    logoPath: new URL("/titleLogo.png", import.meta.url).href,
    pic1: new URL("@/assets/images/pic1_1.png", import.meta.url).href,
    pic2: new URL("@/assets/images/pic1_2_1.png", import.meta.url).href,
    pic3: new URL("@/assets/images/pic1_2_2.png", import.meta.url).href,
    pic4: new URL("@/assets/images/pic1_2.png", import.meta.url).href,
    pic5: new URL("@/assets/images/pic1_3.png", import.meta.url).href,
    pic6: new URL("@/assets/images/pic2_1.png", import.meta.url).href,
    pic7: new URL("@/assets/images/pic2_2.png", import.meta.url).href,
    pic8: new URL("@/assets/images/pic2_3.png", import.meta.url).href,
    pic9: new URL("@/assets/images/pic3_1.png", import.meta.url).href,
    pic10: new URL("@/assets/images/pic3_2.png", import.meta.url).href,
    pic11: new URL("@/assets/images/pic3_3.png", import.meta.url).href,
    pic12: new URL("@/assets/images/pic3_4.png", import.meta.url).href,
    pic13: new URL("@/assets/images/pic3_5.png", import.meta.url).href,
    pic14: new URL("@/assets/images/pic3_6.png", import.meta.url).href,
  }),

  components: {},
};
</script>
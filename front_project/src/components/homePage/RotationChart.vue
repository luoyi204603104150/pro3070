<template>
  <div class="carousel-container">
    <el-carousel type="card" height="300px" :interval="3000" arrow="always">
      <el-carousel-item v-for="(image, index) in images" :key="index">
        <img :src="image.src" :alt="image.alt" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
const images = [
  { src: '/src/assets/homePage/1.jpg', alt: 'image1' },
  { src: '/src/assets/homePage/2.jpg', alt: 'image2' },
  { src: '/src/assets/homePage/3.jpg', alt: 'image3' },
]
</script>

<style scoped>
.carousel-container {
  width: 100%;
}

img {
  width: 100%;
  height: 300px;
}
</style>
<template>
  <div class="my-10">
    <RegisterForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import RegisterForm from '@/components/RegisterForm.vue'

export default {
  name: 'RegisterPage',
  components: {
    RegisterForm
  }
}
</script>
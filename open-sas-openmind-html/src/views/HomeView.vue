<template>
  <div
      id="container"
      class="w-screen h-screen flex items-center justify-center mw-regular relative"
  >
    <div id="sub-container" class="w-1/2 h-2/3 bg-white rounded-2xl opacity-90 p-8">
      <div>
        <div class="text-xl">Token</div>
        <div class="h-4"></div>
        <el-button @click="getToken">申请 Token（时效 3 个月）</el-button>
        <span class="mx-3"></span>
        <el-tooltip
            class="box-item"
            effect="dark"
            content="点击复制"
            placement="top"
        >
          <span @click="copyToken">{{ token }}</span>
        </el-tooltip>
      </div>

      <div class="sep-line"></div>

      <div>
        <div class="text-xl">Channel 元信息</div>
        <div class="h-4"></div>
        <el-tree :data="tree" :props="defaultProps"/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, reactive} from "vue";
import axios from "axios";
import {requestUrl} from "@/http";

interface ChannelMetaInfo {
  groups: Set<GroupMetaInfo>,
}

interface GroupMetaInfo {
  group: string,
  topics: Set<TopicMetaInfo>,
}

interface TopicMetaInfo {
  topic: string,
  priorities: Set<PriorityMetaInfo>,
}

interface PriorityMetaInfo {
  priority: number,
  versions: Set<string>,
}

const defaultProps = {
  children: 'children',
  label: 'label',
}

interface Tree {
  label: string
  children: Tree[]
}

const tree: Tree[] = reactive([])

const getMetaInfo = () => {
  axios
      .get(`${requestUrl}/channel/meta`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }
      })
      .then(res => {
        const data: ChannelMetaInfo = res.data.data;

        console.log(data)

        data.groups.forEach(group => {
          const groupSubTree: Tree = {
            label: `[Group] ${group.group}`,
            children: [],
          }

          group.topics.forEach(topic => {
            const topicSubTree: Tree = {
              label: `[Topic] ${topic.topic}`,
              children: [],
            }

            topic.priorities.forEach(priority => {
              const prioritySubTree: Tree = {
                label: `[Priority] ${priority.priority}`,
                children: [],
              }

              priority.versions.forEach(version => {
                prioritySubTree.children.push({
                  label: `[Version] ${version}`,
                  children: [],
                })
              })

              topicSubTree.children.push(prioritySubTree)
            })

            groupSubTree.children.push(topicSubTree)
          })

          tree.push(groupSubTree)
        })
      })
}

onMounted(() => getMetaInfo());

let token = ref<string>("NO TOKEN NOW")

const getToken = () => {
  axios
      .post(`${requestUrl}/channel/token`, {
        userId: Number(localStorage.getItem("userId")),
      }, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }
      })
      .then(res => {
        token.value = res.data.data
        console.log(token)
      })
}

const copyToken = () => {
  navigator.clipboard.writeText(token.value)
}

</script>

<style scoped>
@import url(/src/assets/font.css);

#container {
  background-image: url("/public/img/post.jpeg");
}

#sub-container {

}

.sep-line {
  font-size: 0.4rem;
  color: gray;
  border: none;
  border-top: 1px solid gray;
  margin-top: 30px;
  margin-bottom: 30px;
  width: 90%;
  margin-left: 50%;
  transform: translateX(-50%);
}

</style>

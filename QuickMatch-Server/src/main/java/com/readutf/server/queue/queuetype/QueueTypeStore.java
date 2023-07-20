package com.readutf.server.queue.queuetype;

import com.readutf.quickmatch.shared.queue.QueueType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueTypeStore extends MongoRepository<QueueType, String> {


}
